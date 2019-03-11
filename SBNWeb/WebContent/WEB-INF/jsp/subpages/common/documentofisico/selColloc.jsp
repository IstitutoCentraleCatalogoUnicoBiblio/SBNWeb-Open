<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<table width="100%" border="0">
	<tr class="etichetta">
		<td colspan="6">
		<table width="100%">
			<tr>
				<c:choose>
					<c:when test="${currentForm.class.simpleName ne 'EsameCollocRicercaForm'}">
						<td width="14%"><bean:message key="documentofisico.dallaSezioneT"
							bundle="documentoFisicoLabels" /></td>
					</c:when>
					<c:otherwise>
						<td width="14%"><bean:message key="documentofisico.sezioneT"
							bundle="documentoFisicoLabels" /></td>
					</c:otherwise>
				</c:choose>
				<td colspan="2"><html:text disabled="${currentForm.disableSez}"
					styleId="testoNormale" property="sezione" size="15" maxlength="10"></html:text><html:messages
					id="msg1" message="true" property="documentofisico.parameter.bottone"
					bundle="documentoFisicoLabels" /> <html:submit title="Lista Sezioni"
					styleClass="buttonImageListaSezione" property="${msg1}">
					<bean:message key="documentofisico.lsSez" bundle="documentoFisicoLabels" />
				</html:submit></td>
				<c:choose>
					<c:when test="${currentForm.class.simpleName eq 'EsameCollocRicercaForm'}">
						<td>
						<div align="center" class="etichetta">Esatto</div>
						</td>
					</c:when>
				</c:choose>
			</tr>
			<c:choose>
				<c:when test="${currentForm.class.simpleName eq 'EsameCollocRicercaForm'}">
					<tr>
						<td>
						<div align="left" class="etichetta"><bean:message
							key="documentofisico.collocazioneT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td colspan="2" valign="middle" class="testo"><html:text
							disabled="${currentForm.disableDallaColl}" styleId="testoNormale"
							property="dallaCollocazione" size="30" maxlength="24"></html:text> <html:messages
							id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" /><html:messages id="msg1" message="true"
							property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
							title="Lista Collocazioni" styleClass="buttonImageListaSezione" property="${msg1}">
							<bean:message key="documentofisico.lsColl" bundle="documentoFisicoLabels" />
						</html:submit></td>
						<td>
						<div align="center"><html:checkbox disabled="${currentForm.disableEsattoColl}"
							property="esattoColl"></html:checkbox><html:hidden property="esattoColl"
							value="false" /></div>
						</td>
						<td width="30%">
						<div align="center"></div>
						</td>
					</tr>
					<tr>
						<td>
						<div align="left" class="etichetta"><bean:message
							key="documentofisico.specificazioneT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td colspan="2" valign="middle" class="testo"><html:text
							disabled="${currentForm.disableDallaSpec}" styleId="testoNormale"
							property="dallaSpecificazione" size="20" maxlength="12"></html:text> <html:messages
							id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" /><html:messages id="msg1" message="true"
							property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
							title="Lista Specificazioni" styleClass="buttonImageListaSezione"
							property="${msg1}">
							<bean:message key="documentofisico.lsSpec" bundle="documentoFisicoLabels" />
						</html:submit></td>
						<td>
						<div align="center"><html:checkbox disabled="${currentForm.disableEsattoSpec}"
							property="esattoSpec"></html:checkbox><html:hidden property="esattoSpec"
							value="false" /></div>
						</td>
						<td width="30%">
						<div align="center"></div>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td width="14%"><bean:message key="documentofisico.dallaCollocazioneT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text disabled="${currentForm.disableDallaColl}"
							styleId="testoNormale" property="dallaCollocazione" size="30" maxlength="24"></html:text>
						<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" /> <html:submit title="Lista Collocazioni"
							styleClass="buttonImageListaSezione" property="${msg1}">
							<bean:message key="documentofisico.lsColl" bundle="documentoFisicoLabels" />
						</html:submit></td>
						<td><bean:message key="documentofisico.specificazioneT"
							bundle="documentoFisicoLabels" /><html:text
							disabled="${currentForm.disableDallaSpec}" styleId="testoNormale"
							property="dallaSpecificazione" size="18" maxlength="12"></html:text> <html:messages
							id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" /> <html:submit title="Lista Specificazioni"
							styleClass="buttonImageListaSezione" property="${msg1}">
							<bean:message key="documentofisico.lsSpec" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
					<tr>
						<td width="14%"><bean:message key="documentofisico.allaCollocazioneT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text disabled="${currentForm.disableAllaColl}" styleId="testoNormale"
							property="allaCollocazione" size="30" maxlength="24"></html:text> <html:messages
							id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" /> <html:submit title="Lista Collocazioni"
							styleClass="buttonImageListaSezione" property="${msg1}">
							<bean:message key="documentofisico.lsCollA" bundle="documentoFisicoLabels" />
						</html:submit></td>
						<td><bean:message key="documentofisico.specificazioneT"
							bundle="documentoFisicoLabels" /><html:text
							disabled="${currentForm.disableAllaSpec}" styleId="testoNormale"
							property="allaSpecificazione" size="18" maxlength="12"></html:text> <html:messages
							id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" /> <html:submit title="Lista Specificazioni"
							styleClass="buttonImageListaSezione" property="${msg1}">
							<bean:message key="documentofisico.lsSpecA" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</table>
		</td>
	</tr>
</table>
