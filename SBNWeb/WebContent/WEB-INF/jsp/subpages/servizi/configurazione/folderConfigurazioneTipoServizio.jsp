<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<div style="width:100%;" class="SchedaImg1">
	<div style="width:25%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneTipoServizioForm.folder eq 'S'}">
				<div class="schedaOn" style="text-align: center;">
				    <%-- <bean:message key="servizi.bottone.servizi" bundle="serviziLabels" /> --%>
				    <bean:message key="servizi.bottone.diritti" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazioneTipoServizio" disabled="${ConfigurazioneTipoServizioForm.conferma}" styleClass="sintButtonLinkDefault">
						<%-- <bean:message key="servizi.bottone.servizi" bundle="serviziLabels" /> --%>
						<bean:message key="servizi.bottone.diritti" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:25%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneTipoServizioForm.folder eq 'I'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.iter" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazioneTipoServizio" disabled="${ConfigurazioneTipoServizioForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.iter" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:25%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneTipoServizioForm.folder eq 'M'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.modalitaErogazione" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazioneTipoServizio" disabled="${ConfigurazioneTipoServizioForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.modalitaErogazione" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:25%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneTipoServizioForm.folder eq 'R'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.moduloRichiesta" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazioneTipoServizio" disabled="${ConfigurazioneTipoServizioForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.moduloRichiesta" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

</div>
