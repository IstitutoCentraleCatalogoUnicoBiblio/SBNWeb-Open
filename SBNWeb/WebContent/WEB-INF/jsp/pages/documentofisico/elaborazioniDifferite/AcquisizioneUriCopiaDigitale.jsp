<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<layout:page>
	<sbn:navform
		action="/documentofisico/elaborazioniDifferite/acquisizioneUriCopiaDigitale.do"
		enctype="multipart/form-data">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<table width="100%">
				<tr>
					<td><bean:message key="label.selezionafile"
							bundle="esportaLabels" /></td>
					<td><html:file property="input" />&nbsp;
					<html:submit property="methodAcqUriCopiaDigitale">
							<bean:message key="button.caricafile" bundle="esportaLabels" />
						</html:submit></td>

					<td colspan="2" width="40%">&nbsp;</td>
				</tr>
			</table>

			<p style="margin:2em;">&nbsp;</p>

			<div style="width: 100%;" class="SchedaImg1">

				<div style="width: 45%; float: left;">
					<c:choose>
						<c:when test="${navForm.folderJSP eq 'WITHOUT_URI'}">
							<div class="schedaOn" style="text-align: center;">
								<bean:message key="folder.documentofisico.uri.generaUri"
									bundle="documentoFisicoLabels" />
							</div>
						</c:when>
						<c:otherwise>
							<div class="schedaOff" align="center">
								<html:submit style="margin-left:auto; margin-right:auto;"
									property="methodAcqUriCopiaDigitale"
									styleClass="sintButtonLinkDefault">
									<bean:message key="folder.documentofisico.uri.generaUri"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div style="width: 45%; float: left;">
					<c:choose>
						<c:when test="${navForm.folderJSP eq 'WITH_URI'}">
							<div class="schedaOn" style="text-align: center;">
								<bean:message key="folder.documentofisico.uri.acquisisciUri"
									bundle="documentoFisicoLabels" />
							</div>
						</c:when>
						<c:otherwise>
							<div class="schedaOff" align="center">
								<html:submit style="margin-left:auto; margin-right:auto;"
									property="methodAcqUriCopiaDigitale"
									styleClass="sintButtonLinkDefault">
									<bean:message key="folder.documentofisico.uri.acquisisciUri"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

			<c:choose>
				<c:when test="${navForm.folderJSP eq 'WITHOUT_URI'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/documentofisico/elaborazioniDifferite/folderGenera_AUCD.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/documentofisico/elaborazioniDifferite/folderAcquisisci_AUCD.jsp" />
				</c:otherwise>
			</c:choose>

			<hr style="width:90%; float:left; margin-top:1em; margin-bottom:1em;" />

			<table width="100%">
				<tr>
					<td width="20%"><bean:message
							key="documentofisico.tipoDigitalizzazioneT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="richiesta.tipoDigit">
							<html:optionsCollection property="listaTipoDigit"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td width="20%"><bean:message
							key="documentofisico.rifTecaDigitaleT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="richiesta.tecaDigitale">
							<html:optionsCollection property="listaTeche"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td width="20%"><bean:message
							key="documentofisico.dispDaRemotoT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="richiesta.dispDaRemoto">
							<html:optionsCollection property="listaDispDaRemoto"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td width="20%"><bean:message
							key="label.documentofisico.uri.preparaFileIndice"
							bundle="documentoFisicoLabels" /></td>
					<td><html:checkbox property="richiesta.preparaFileIndice" />
						<html:hidden property="richiesta.preparaFileIndice" value="false" />
					</td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td width="20%"><bean:message
							key="label.documentofisico.uri.modalitaTrattamentoUri"
							bundle="documentoFisicoLabels" /></td>
					<td><bean:message key="label.documentofisico.uri.aggiungiUri"
							bundle="documentoFisicoLabels" /> <html:radio
							property="richiesta.aggiungiUri" value="true" /> <bean:message
							key="label.documentofisico.uri.sostituisciUri"
							bundle="documentoFisicoLabels" /> <html:radio
							property="richiesta.aggiungiUri" value="false" /></td>
					<td colspan="3"></td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
