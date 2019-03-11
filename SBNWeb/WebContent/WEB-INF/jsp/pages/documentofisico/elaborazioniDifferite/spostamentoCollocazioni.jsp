<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/elaborazioniDifferite/spostamentoCollocazioni.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit  title="Lista Biblioteche" styleClass="buttonImageListaSezione" disabled="false"
					property="methodSpostamentoColloc">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="spostamentoCollocazioniForm"
					property="descrBib" /></td>
			</tr>
		</table>
		<!--  spostamento inventari -->
		<table width="100%">
			<tr class="etichetta">
				<th>Spostamento Inventari</th>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="30">
				<c:choose>
					<c:when test="${spostamentoCollocazioniForm.folder eq 'RangeInv'}">
						<td width="33%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodSpostamentoColloc"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${spostamentoCollocazioniForm.folder eq 'Collocazioni'}">
						<td width="33%" class="schedaOn">
						<div align="center">Collocazione</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodSpostamentoColloc"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerCollocazione"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${spostamentoCollocazioniForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${spostamentoCollocazioniForm.folder eq 'Collocazioni'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<%--
			</tr>
			<tr class="etichetta">
				<td colspan="6">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td width="18%">
						<div align="right" class="etichetta"><bean:message key="documentofisico.serieT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td><html:select property="codSerie">
							<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
						</html:select></td>
						<td><bean:message key="documentofisico.dalNumero" bundle="documentoFisicoLabels" />
						<html:text styleId="testoNormale" property="dalNum" size="10" maxlength="9"></html:text></td>
						<td colspan="4"><bean:message key="documentofisico.alNumero"
							bundle="documentoFisicoLabels" /> <html:text styleId="testoNormale"
							property="alNum" size="10" maxlength="9"></html:text></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr class="etichetta">
				<td colspan="6">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td width="18%"><bean:message key="documentofisico.dallaSezioneT"
							bundle="documentoFisicoLabels" /></td>
						<td colspan="5"><html:text styleId="testoNormale" property="codSezP" size="15"
							maxlength="10"
							disabled="${spostamentoCollocazioniForm.disableSezP}">
							</html:text><html:submit   title="Lista Sezioni" styleClass="buttonImageListaSezione" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.lsSez" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
					<tr>
						<td width="18%"><bean:message key="documentofisico.dallaCollocazioneT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text styleId="testoNormale" property="dallaCollocazione"
							size="30" maxlength="24"
							disabled="${spostamentoCollocazioniForm.disableDallaColl}"></html:text> <html:submit
							  title="Lista Collocazioni" styleClass="buttonImageListaSezione" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.lsColl" bundle="documentoFisicoLabels" />
						</html:submit></td>
						<td colspan="2"><bean:message key="documentofisico.specificazioneT"
							bundle="documentoFisicoLabels" /><html:text styleId="testoNormale"
							property="dallaSpecificazione" size="18" maxlength="12"
							disabled="${spostamentoCollocazioniForm.disableDallaSpec}"></html:text><html:submit
							  title="Lista Specificazioni" styleClass="buttonImageListaSezione" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.lsSpec" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
					<tr>
						<td width="18%"><bean:message key="documentofisico.allaCollocazioneT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text styleId="testoNormale" property="allaCollocazione"
							size="30" maxlength="24"
							disabled="${spostamentoCollocazioniForm.disableAllaColl}"></html:text> <html:submit
							  title="Lista Collocazioni" styleClass="buttonImageListaSezione" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.lsCollA" bundle="documentoFisicoLabels" />
						</html:submit></td>
						<td colspan="2"><bean:message key="documentofisico.specificazioneT"
							bundle="documentoFisicoLabels" /><html:text styleId="testoNormale"
							property="allaSpecificazione" size="18" maxlength="12"
							disabled="${spostamentoCollocazioniForm.disableAllaSpec}"></html:text><html:submit
							  title="Lista Specificazioni" styleClass="buttonImageListaSezione" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.lsSpecA" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</table>
				</td>
			--%></tr>
			<BR>
			<tr class="etichetta">
				<th>Nuova Collocazione</th>
			</tr>
			<tr class="etichetta">
				<td colspan="6">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td width="18%"><bean:message key="documentofisico.sezioneDiArrivoT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text styleId="testoNormale" property="codSezArrivo" size="15"
							maxlength="10"></html:text><html:submit title="Lista Sezioni"
							styleClass="buttonImageListaSezione" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.lsSezA" bundle="documentoFisicoLabels" />
						</html:submit>&nbsp;&nbsp;&nbsp;<html:submit
							property="methodSpostamentoColloc">
							<bean:message key="documentofisico.nessunaSezione" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
					<tr>
						<td width="18%"><bean:message key="documentofisico.collocazioneProvvisoriaT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text styleId="testoNormale" property="collocazioneProvvisoria"
							size="30" maxlength="24" disabled="${spostamentoCollocazioniForm.disableCollProvv}"></html:text><html:submit
							title="Lista Collocazioni" styleClass="buttonImageListaSezione"
							property="methodSpostamentoColloc"
							disabled="${spostamentoCollocazioniForm.disableTastoCollProvv}">
							<bean:message key="documentofisico.lsCollProvv" bundle="documentoFisicoLabels" />
						</html:submit>&nbsp;&nbsp;&nbsp;<html:submit
							property="methodSpostamentoColloc">
							<bean:message key="documentofisico.nessunaCollocazione" bundle="documentoFisicoLabels" />
						</html:submit></td>
					<tr>
						<td width="18%"><bean:message key="documentofisico.specificazioneProvvisoriaT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text styleId="testoNormale" property="specificazioneProvvisoria"
							size="18" maxlength="12" disabled="${spostamentoCollocazioniForm.disableSpecProvv}"></html:text><html:submit
							title="Lista Specificazioni" styleClass="buttonImageListaSezione"
							property="methodSpostamentoColloc"
							disabled="${spostamentoCollocazioniForm.disableTastoSpecifProvv}">
							<bean:message key="documentofisico.lsSpecProvv" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
					<tr>

						<td colspan="2">
						<div class="nota"><bean:message key="documentofisico.testoAsterisco"
							bundle="documentoFisicoLabels" /></div></td>
					</tr>
					<tr>

						<td colspan="2">
						<div class="nota"><bean:message key="documentofisico.testoDoppioAsterisco"
							bundle="documentoFisicoLabels" /></div></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<table>
			<tr class="etichetta">
				<td width="30%">
				<div align="right" class="etichetta"><bean:message
					key="documentofisico.ancheInvInPrest" bundle="documentoFisicoLabels" /></div>
				</td>
				<td width="5%"><html:checkbox property="prestito" disabled="${spostamentoCollocazioniForm.disable}"></html:checkbox>
				<html:hidden property="prestito" value="false" /></td>
				<td width="30%" class="etichetta">
				<div align="right" class="etichetta"><bean:message
					key="documentofisico.ristampaEtichette" bundle="documentoFisicoLabels" /></div>
				</td>
				<td width="5%"><html:checkbox property="ristampa" disabled="${spostamentoCollocazioniForm.disable}"></html:checkbox>
				<html:hidden property="ristampa" value="false" /></td>
			</tr>
		</table>
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${spostamentoCollocazioniForm.disable eq false}">
						<td><html:submit styleClass="pulsanti" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.bottone.ok" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit styleClass="pulsanti" property="methodSpostamentoColloc">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
