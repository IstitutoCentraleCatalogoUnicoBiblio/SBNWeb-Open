<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/elaborazioniDifferite/scaricoInventariale.do"
		enctype="multipart/form-data">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" disabled="false"
					property="methodScaricoInventariale">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="scaricoInventarialeForm" property="descrBib" /></td>
			</tr>
		</table>

<!-- Modifica almaviva2 su richieste interne 11.07.2012 modificata etichetta Scarico inventariale in Dismissione inventari
-->
		<table width="100%" border="0">
			<tr class="etichetta">
				<th colspan="6">Registra la dismissione degli inventari</th>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td width="14%"><bean:message key="documentofisico.motivoScaricoT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:select property="motivoDelloScarico" onchange="this.form.submit()">
					<html:optionsCollection property="listaMotivoScarico" value="codice"
						label="descrizione" />
				</html:select></td>
				<td><bean:message key="documentofisico.numeroBuonoScaricoT"
					bundle="documentoFisicoLabels" /><html:text styleId="testoNormale"
					property="numBuonoScarico" size="15" maxlength="9"></html:text></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="14%"><bean:message key="documentofisico.dataScaricoT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text styleId="testoNormale" property="dataScarico" size="15"
					maxlength="10"></html:text></td>
				<td><bean:message key="documentofisico.dataDeliberaT"
					bundle="documentoFisicoLabels" /> <html:text styleId="testoNormale"
					property="dataDelibera" size="15" maxlength="10"></html:text></td>
			</tr>
			<tr>
				<td width="14%"><bean:message key="documentofisico.testoDeliberaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:textarea styleId="testoNormale" property="testoDelibera" cols="50"
					rows="1"></html:textarea></td>
				<td>&nbsp;</td>
			</tr>
			<c:choose>
				<c:when test="${scaricoInventarialeForm.trasferimento == true}">
					<tr class="etichetta">
						<td colspan="6">
						<table width="100%"
							style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
							<tr>
								<td width="14%">
								<div align="right" class="etichetta"><bean:message
									key="documentofisico.versoLaBiblioT" bundle="documentoFisicoLabels" /></div>
								</td>
								<td><html:text styleId="testoNormale" property="polo"
									readonly="true" size="5" maxlength="3"
									title="Polo"></html:text>&nbsp; <html:text styleId="testoNormale"
									property="versoBibliotecaDescr" readonly="true"
									size="90" maxlength="80" title="Biblioteca"></html:text><html:submit styleClass="buttonImage"
									property="methodScaricoInventariale" title="Ricerca biblioteca">
									<bean:message key="documentofisico.bottone.SIFbibl"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
						</table>
						</td>
					</tr>
				</c:when>
			</c:choose>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="30">
				<c:choose>
					<c:when test="${scaricoInventarialeForm.folder eq 'RangeInv'}">
						<td width="33%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodScaricoInventariale"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${scaricoInventarialeForm.folder eq 'Inventari'}">
						<td width="33%" class="schedaOn">
						<div align="center">Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodScaricoInventariale" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerInventari" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${scaricoInventarialeForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${scaricoInventarialeForm.folder eq 'Inventari'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${scaricoInventarialeForm.disable == false}">
						<td><html:submit styleClass="pulsanti" property="methodScaricoInventariale">
							<bean:message key="documentofisico.bottone.ok" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodScaricoInventariale">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit styleClass="pulsanti" property="methodScaricoInventariale">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
