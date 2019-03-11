<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<!-- dati di collocazione
<table width="100%" border="0">
	<tr>
		<td class="etichetta">Dati di Collocazione</td>
	</tr>
</table> -->
<br>
<table width="100%" border="0">
	<tr bgcolor="#FFCC99">
		<td width="20%" bgcolor="${color}"><bean:message key="documentofisico.inventario"
			bundle="documentoFisicoLabels" />
		</td>
		<td><bean-struts:write name="currentForm"
			property="codSerie" /> <bean-struts:write name="currentForm" property="codInvent" />
		</td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.sezioneT" bundle="documentoFisicoLabels" />
		</td>
		<td><html:text styleId="testoNormale" property="codSez" size="15" maxlength="10"
			disabled="${currentForm.disablePerModInvDaNav}"></html:text> <html:messages id="msg1"
			message="true" property="documentofisico.parameter.bottone"
			bundle="documentoFisicoLabels" /> <html:submit title="Lista Sezioni"
			styleClass="buttonImageListaSezione" property="${msg1}"
			disabled="${currentForm.disablePerModInvDaNav}">
			<bean:message key="documentofisico.lsSez" bundle="documentoFisicoLabels" />
		</html:submit> <c:choose>
			<c:when test="${currentForm.codSez ne null}">
				<bean-struts:write name="currentForm" property="descrSez" />
			</c:when>
		</c:choose></td>
	</tr>
	<c:choose>
		<c:when test="${currentForm.noSezione eq 'noSezione'}">
		</c:when>
		<c:otherwise>
			<tr>
				<td width="20%"><bean:message key="documentofisico.tipoCollocazioneT"
					bundle="documentoFisicoLabels" /></td>
				<td colspan="7"><bean-struts:write name="currentForm"
					property="descrTipoCollocazione" /> <c:choose>
					<c:when test="${currentForm.descrTipoCollocazione eq 'sistema di classificazione'}">
						<%--<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone"
							bundle="documentoFisicoLabels" />--%>
							<html:submit property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsCl" bundle="documentoFisicoLabels" /></html:submit>
						<%--
						<sbn:checkAttivita idControllo="soggetti">
							<html:submit property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsCl" bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita>
					--%>
					</c:when>
				</c:choose></td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${currentForm.descrTipoCollocazione eq 'sistema di classificazione'}">
						<table width="100%" border="0">
							<tr>
								<td width="20%"><bean:message key="documentofisico.collocazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td colspan="2"><html:text styleId="testoNormale" property="codCollocazione" size="30"
									maxlength="24"></html:text> <html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
							<tr>
								<td><bean:message key="documentofisico.specificazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td colspan="2"><html:text styleId="testoNormale"
									disabled="${currentForm.codSpecificazioneDisable}" property="codSpecificazione"
									size="20" maxlength="12"></html:text><html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
							<tr>
							<td></td>
								<td width="25%" align="right"><bean:message key="documentofisico.chiaveTitoloT"
									bundle="documentoFisicoLabels" /><html:radio property="chiave"
									value="chiaveTitolo" onchange="this.form.submit()"
									disabled="${currentForm.disablePerModInvDaNav}" /></td>
							<td></td>
							</tr>
							<tr>
							<td></td>
								<td width="25%" align="right"><bean:message key="documentofisico.chiaveAutoreT"
									bundle="documentoFisicoLabels" /><html:radio property="chiave"
									value="chiaveAutore" onchange="this.form.submit()"
									disabled="${currentForm.disablePerModInvDaNav}" /></td>
							<td></td>
							</tr>
							<tr>
								<td></td>
								<td width="25%" align="right"><bean:message key="documentofisico.chiaveAltroT"
									bundle="documentoFisicoLabels" /><html:radio property="chiave" value="altro"
									onchange="this.form.submit()" disabled="${currentForm.disablePerModInvDaNav}" /></td>
							<td></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${currentForm.descrTipoCollocazione eq 'chiave titolo'}">
						<table>
							<tr>
								<td width="36%"><bean:message key="documentofisico.collocazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td colspan="3"><html:text disabled="${currentForm.codCollocazioneDisable}"
									styleId="testoNormale" property="codCollocazione" size="30" maxlength="24"></html:text>
								<html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
							<tr>
								<td width="36%"><bean:message key="documentofisico.specificazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:text styleId="testoNormale"
									disabled="${currentForm.codSpecificazioneDisable}" property="codSpecificazione"
									size="20" maxlength="12"></html:text><html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
								</html:submit></td>
								<td><bean:message key="documentofisico.chiaveTitoloT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:radio property="chiave" value="chiaveTitolo"
									onchange="this.form.submit()" disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
							<tr>
								<td></td>
								<td colspan="2"><bean:message key="documentofisico.chiaveAutoreT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:radio property="chiave" value="chiaveAutore"
									onchange="this.form.submit()" disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
							<tr>
								<td></td>
								<td colspan="2"><bean:message key="documentofisico.chiaveAltroT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:radio property="chiave" value="altro" onchange="this.form.submit()"
									disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${currentForm.descrTipoCollocazione eq 'esplicita non strutturata'}">
						<table>
							<tr>
								<td width="36%"><bean:message key="documentofisico.collocazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td colspan="3"><html:text styleId="testoNormale" property="codCollocazione"
									size="30" maxlength="24"></html:text> <html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
							<tr>
								<td width="36%"><bean:message key="documentofisico.specificazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:text styleId="testoNormale"
									disabled="${currentForm.codSpecificazioneDisable}" property="codSpecificazione"
									size="20" maxlength="12"></html:text><html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
								</html:submit></td>
								<td><bean:message key="documentofisico.chiaveTitoloT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:radio property="chiave" value="chiaveTitolo"
									onchange="this.form.submit()" disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
							<tr>
								<td></td>
								<td colspan="2" align="right"><bean:message
									key="documentofisico.chiaveAutoreT" bundle="documentoFisicoLabels" /></td>
								<td><html:radio property="chiave" value="chiaveAutore"
									onchange="this.form.submit()" disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
							<tr>
								<td></td>
								<td colspan="2" align="right"><bean:message
									key="documentofisico.chiaveAltroT" bundle="documentoFisicoLabels" /></td>
								<td><html:radio property="chiave" value="altro" onchange="this.form.submit()"
									disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${currentForm.descrTipoCollocazione eq 'esplicita strutturata'}">
						<table>
							<tr>
								<td width="29%"><bean:message key="documentofisico.collocazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td colspan="3"><bean:message key="documentofisico.livello1T"
									bundle="documentoFisicoLabels" /> <html:text styleId="testoNormale"
									property="livello1" size="10" maxlength="7"></html:text><bean:message
									key="documentofisico.livello2T" bundle="documentoFisicoLabels" /> <html:text
									styleId="testoNormale" property="livello2" size="10" maxlength="7"></html:text><bean:message
									key="documentofisico.livello3T" bundle="documentoFisicoLabels" /> <html:text
									styleId="testoNormale" property="livello3" size="10" maxlength="8"></html:text><html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
							<tr>
								<td width="29%"><bean:message key="documentofisico.specificazioneT"
									bundle="documentoFisicoLabels" /></td>
								<td><html:text styleId="testoNormale"
									disabled="${currentForm.codSpecificazioneDisable}" property="codSpecificazione"
									size="20" maxlength="12"></html:text><html:messages id="msg1" message="true"
									property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
									property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
								</html:submit></td>
								<td align="right"><bean:message key="documentofisico.chiaveTitoloT"
									bundle="documentoFisicoLabels" /><html:radio property="chiave"
									value="chiaveTitolo" onchange="this.form.submit()"
									disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
							<tr>
								<td colspan="2"></td>
								<td align="right"><bean:message key="documentofisico.chiaveAutoreT"
									bundle="documentoFisicoLabels" /><html:radio property="chiave"
									value="chiaveAutore" onchange="this.form.submit()"
									disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
							<tr>
								<td colspan="2"></td>
								<td align="right"><bean:message key="documentofisico.chiaveAltroT"
									bundle="documentoFisicoLabels" /><html:radio property="chiave" value="altro"
									onchange="this.form.submit()" disabled="${currentForm.disablePerModInvDaNav}" /></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${currentForm.descrTipoCollocazione eq 'magazzino formato'}">
						<tr>
							<td width="20%"><bean:message key="documentofisico.formatoT"
								bundle="documentoFisicoLabels" /></td>
							<td colspan="2"><html:select property="codFormato"
								onchange="this.form.submit()" style="width:55px"
								disabled="${currentForm.disablePerModInvDaNav}">
								<html:optionsCollection property="listaCodiciFormati" value="codFormato"
									label="descrizioneEstesa" />
							</html:select><html:text disabled="true" styleId="testoNormale"
								property="recFormatiSezioni.descrFor" size="50" maxlength="30"></html:text><html:submit
								property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
							</html:submit><html:submit property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
							</html:submit></td>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.serieT" bundle="documentoFisicoLabels" />
							</td>
							<td colspan="2"><html:text styleId="testoNormale"
								property="recFormatiSezioni.progSerie" size="10" maxlength="10"
								disabled="${currentForm.disablePerModInvDaNav}"></html:text></td>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.progressivoT"
								bundle="documentoFisicoLabels" /></td>
							<td colspan="2"><html:text styleId="testoNormale"
								property="recFormatiSezioni.progNum" size="10" maxlength="6"
								disabled="${currentForm.disablePerModInvDaNav}"></html:text></td>
						</tr>
					</c:when>
					<c:when test="${currentForm.descrTipoCollocazione eq 'continuazione'}">
						<tr>
							<td width="20%"><bean:message key="documentofisico.formatoT"
								bundle="documentoFisicoLabels" /></td>
							<td colspan="2"><html:select property="codFormato"
								onchange="this.form.submit()" style="width:55px"
								disabled="${currentForm.disablePerModInvDaNav}">
								<html:optionsCollection property="listaCodiciFormati" value="codFormato"
									label="descrizioneEstesa" />
							</html:select><html:text disabled="true" styleId="testoNormale"
								property="recFormatiSezioni.descrFor" size="50" maxlength="30"></html:text><html:submit
								property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
							</html:submit><html:submit property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
							</html:submit></td>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.serieT" bundle="documentoFisicoLabels" />
							</td>
							<td colspan="2"><html:text styleId="testoNormale"
								property="recFormatiSezioni.progSerie" size="10" maxlength="10"
								disabled="${currentForm.disablePerModInvDaNav}"></html:text></td>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.progressivoT"
								bundle="documentoFisicoLabels" /></td>
							<td colspan="2"><html:text styleId="testoNormale"
								property="recFormatiSezioni.progNum" size="10" maxlength="6"
								disabled="${currentForm.disablePerModInvDaNav}"></html:text></td>
						</tr>
					</c:when>
					<c:when test="${currentForm.descrTipoCollocazione eq 'magazzino non a formato'}">
						<tr>
							<td width="20%"><bean:message key="documentofisico.progressivoT"
								bundle="documentoFisicoLabels" /></td>
							<td colspan="2"><html:text styleId="testoNormale"
								property="recFormatiSezioni.progNum" size="10" maxlength="10"></html:text><html:submit
								property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsUC" bundle="documentoFisicoLabels" />
							</html:submit><html:submit property="${msg1}" disabled="${currentForm.disablePerModInvDaNav}">
								<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
							</html:submit></td>
						</tr>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</tr>
		</c:otherwise>
	</c:choose>
</table>
