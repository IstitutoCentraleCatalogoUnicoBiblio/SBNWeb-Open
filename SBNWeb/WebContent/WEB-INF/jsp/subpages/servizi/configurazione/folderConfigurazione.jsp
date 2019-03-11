<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<div style="width:100%;" class="SchedaImg1">
	<div style="width:20%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneForm.folder eq 'T'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.tipiServizio" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazione" disabled="${ConfigurazioneRicercaForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.tipiServizio" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:20%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneForm.folder eq 'E'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.modalitaErogazione" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazione" disabled="${ConfigurazioneRicercaForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.modalitaErogazione" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:20%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneForm.folder eq 'S'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.supporti" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazione" disabled="${ConfigurazioneRicercaForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.supporti" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:20%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneForm.folder eq 'P'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.parametriBiblioteca" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazione" disabled="${ConfigurazioneRicercaForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.parametriBiblioteca" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:20%; float:left;">
		<c:choose>
			<c:when test="${ConfigurazioneForm.folder eq 'M'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.bottone.modalitaPagamento" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodConfigurazione" disabled="${ConfigurazioneRicercaForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.bottone.modalitaPagamento" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
