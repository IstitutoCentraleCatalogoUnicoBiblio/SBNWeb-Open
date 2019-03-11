<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/datiInventari/vaiAInserimentoColl.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="3">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text readonly="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text> <bean-struts:write
					name="vaiAInserimentoCollForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<br>
		<c:choose>
			<c:when test="${vaiAInserimentoCollForm.prov eq 'CV'}">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td><bean:message key="documentofisico.titoloInColl"
							bundle="documentoFisicoLabels" />: <span class="etichetta"></span> <bean-struts:write
							name="vaiAInserimentoCollForm" property="bid" />&nbsp;&nbsp;&nbsp;<bean-struts:write
							name="vaiAInserimentoCollForm" property="titolo" /></td>
					</tr>
				</table>
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/vaiAInserimentoCollTab1.jsp" />
			</c:when>
			<c:otherwise>
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td><bean:message key="documentofisico.notiziaCorrT"
							bundle="documentoFisicoLabels" />: <span class="etichetta"></span> <bean-struts:write
							name="vaiAInserimentoCollForm" property="bid" />&nbsp;&nbsp;&nbsp;<bean-struts:write
							name="vaiAInserimentoCollForm" property="titolo" /></td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0">
					<tr height="30">
						<c:choose>
							<c:when test="${vaiAInserimentoCollForm.folder eq 'tab1'}">
								<td width="27%" class="schedaOn">
								<div align="center">Nuova Collocazione</div>
								</td>
							</c:when>
							<c:otherwise>
								<td width="27%" class="schedaOff">
								<div align="center"><html:submit property="methodVaiAInsColl"
									styleClass="sintButtonLinkDefault" disabled="${vaiAInserimentoCollForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.insCollTab1" bundle="documentoFisicoLabels" />
								</html:submit></div>
								</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${vaiAInserimentoCollForm.folder eq 'tab2'}">
								<td width="39%" class="schedaOn">
								<div align="center">Colloc. Presenti nel Reticolo</div>
								</td>
							</c:when>
							<c:otherwise>
								<td width="39%" class="schedaOff">
								<div align="center"><html:submit property="methodVaiAInsColl"
									styleClass="sintButtonLinkDefault" disabled="${vaiAInserimentoCollForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.insCollTab2" bundle="documentoFisicoLabels" />
								</html:submit></div>
								</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${vaiAInserimentoCollForm.folder eq 'tab3'}">
								<td width="39%" class="schedaOn">
								<div align="center">Nuova Colloc. a livelli</div>
								</td>
							</c:when>
							<c:otherwise>
								<td width="39%" class="schedaOff">
								<div align="center"><html:submit property="methodVaiAInsColl"
									styleClass="sintButtonLinkDefault" disabled="${vaiAInserimentoCollForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.insCollTab3" bundle="documentoFisicoLabels" />
								</html:submit></div>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</table>
				<c:choose>
					<c:when test="${vaiAInserimentoCollForm.folder eq 'tab1'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/vaiAInserimentoCollTab1.jsp" />
					</c:when>
					<c:when test="${vaiAInserimentoCollForm.folder eq 'tab2'}">
						<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
							totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
							parameter="methodVaiAInsColl"></sbn:blocchi>
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/vaiAInserimentoCollTab2.jsp" />
						<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
							totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
							parameter="methodVaiAInsColl" bottom="true"></sbn:blocchi>
					</c:when>
					<c:when test="${vaiAInserimentoCollForm.folder eq 'tab3'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/vaiAInserimentoCollTab3.jsp" />
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose></div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<td><c:choose>
					<c:when test="${vaiAInserimentoCollForm.conferma}">
						<table align="center">
							<tr>
								<td><html:submit styleClass="pulsanti" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
								</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${vaiAInserimentoCollForm.prov eq 'CV'}">
						<table align="center">
							<tr>
								<td><html:submit styleClass="pulsanti" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.salva" bundle="documentoFisicoLabels" />
								</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
						</table>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${vaiAInserimentoCollForm.folder eq 'tab1'}">
								<html:submit styleClass="pulsanti" property="methodVaiAInsColl"
								disabled="${vaiAInserimentoCollForm.disableTastoAvanti}">
									<bean:message key="documentofisico.bottone.avanti" bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:when test="${vaiAInserimentoCollForm.folder eq 'tab2'}">
								<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.sceltaColloc"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.esEsempl"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.inventari"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:when test="${vaiAInserimentoCollForm.folder eq 'tab3'}">
								<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAInsColl">
									<bean:message key="documentofisico.bottone.sceltaTit"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<html:submit styleClass="pulsanti" property="methodVaiAInsColl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
