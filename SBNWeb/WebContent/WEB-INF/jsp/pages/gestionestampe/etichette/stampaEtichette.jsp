<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionestampe/etichette/stampaEtichette.do"
		enctype="multipart/form-data">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" align="center">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaEtichette">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaEtichetteForm" property="descrBib" /></td>
			</tr>
		</table>
		<BR>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th colspan="5" class="etichetta" width="50%">Dati di estrazione</th>
			</tr>
			<tr height="30">
				<c:choose>
					<c:when test="${stampaEtichetteForm.folder eq 'RangeInv'}">
						<td width="33%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaEtichette"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaEtichetteForm.folder eq 'Collocazioni'}">
						<td width="33%" class="schedaOn">
						<div align="center">Collocazione</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaEtichette"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerCollocazione"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaEtichetteForm.folder eq 'Inventari'}">
						<td width="33%" class="schedaOn">
						<div align="center">Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodStampaEtichette" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerInventari" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${stampaEtichetteForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${stampaEtichetteForm.folder eq 'Collocazioni'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
			</c:when>
			<c:when test="${stampaEtichetteForm.folder eq 'Inventari'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<HR>
		<table width="100%" border="0">
			<tr>
				<td colspan = "2" align="right"><bean:message key="documentofisico.numCopieT" bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="false" property="numCopie" size="5" maxlength="2"></html:text></td>
			</tr>
			<tr>
				<td width = "2%"><html:checkbox disabled="${disableModConfig}"
							property="modConfig"></html:checkbox><html:hidden property="modConfig"
							value="false" /></td>
				<td width = "15%"><bean:message key="documentofisico.formatoEtichettaT"
					bundle="documentoFisicoLabels" /></td>
				<td width = "83%"><html:text disabled="false" property="codModello" size="50"
					maxlength="30"></html:text><html:submit property="methodStampaEtichette">
					<bean:message key="documentofisico.lsModelli" bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<tr>
				<td><html:checkbox disabled="${disableModBarCode}"
							property="modBarCode"></html:checkbox><html:hidden property="modBarCode"
							value="false" /></td>
				<td colspan = "2"><bean:message key="documentofisico.formatoEtichettaBarCodeT"
					bundle="documentoFisicoLabels" />&nbsp;&nbsp;&nbsp;&nbsp;Formato&nbsp;di&nbsp;stampa&nbsp;Acrobat&nbsp;&nbsp;(PDF)&nbsp;&nbsp;</td>
			</tr>
		</table>
		<BR>
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaEtichetteForm.disable == false}">
						<td><html:submit property="methodStampaEtichette">
							<bean:message key="documentofisico.bottone.conferma" bundle="documentoFisicoLabels" />
						</html:submit><html:submit property="methodStampaEtichette">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaEtichette">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
