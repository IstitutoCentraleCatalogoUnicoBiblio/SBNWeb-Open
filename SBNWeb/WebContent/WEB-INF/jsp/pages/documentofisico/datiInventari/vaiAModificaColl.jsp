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
	<sbn:navform action="/documentofisico/datiInventari/vaiAModificaColl.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="3">
				<div class="etichetta"><bean:message
					key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />
				<html:text readonly="true" styleId="testoNormale" property="codBib"
					size="5" maxlength="3"></html:text> <bean-struts:write
					name="vaiAModificaCollForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<table width="100%"
			style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
			<tr>
				<td width="18%"><bean:message key="documentofisico.notiziaCorrT"
					bundle="documentoFisicoLabels" /></td>
				<td><bean-struts:write name="vaiAModificaCollForm"
					property="bid" />&nbsp;&nbsp;<bean-struts:write name="vaiAModificaCollForm"
					property="titolo" /></td>
			</tr>
			<tr>
				<td><bean:message key="documentofisico.bidCollocazioneT"
					bundle="documentoFisicoLabels" /></td>
				<td><bean-struts:write name="vaiAModificaCollForm"
					property="recCollDett.bid" />&nbsp;&nbsp;<bean-struts:write
					name="vaiAModificaCollForm" property="recCollDett.bidDescr" /></td>
			</tr>
		</table>
		<jsp:include
				page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/datiCollocazione.jsp" />
		<table width="100%">
			<tr>
				<td>
				<div align="right" class="etichetta"><bean:message
					key="documentofisico.totInvT" bundle="documentoFisicoLabels" /></div>
				</td>
				<td></td>
				<td><bean-struts:write name="vaiAModificaCollForm"
					property="recCollDett.totInv" />

				</td>
			</tr>
			<tr>
				<td>
				<div align="right" class="etichetta"><bean:message
					key="documentofisico.consistenzaT" bundle="documentoFisicoLabels" /></div>
				</td>
				<td></td>
				<td><html:textarea styleId="testoNormale" property="consistenza" rows="5" cols="70"></html:textarea></td>
			</tr>
		</table>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${vaiAModificaCollForm.conferma}">
						<td><html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.modificaInv"
								bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.esemplare"
								bundle="documentoFisicoLabels" />
						</html:submit> <c:choose>
							<c:when test="${vaiAModificaCollForm.abilitaBottoneInviaInIndice}">
								<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAModColl">
									<bean:message key="documentofisico.bottone.indice" bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
						</c:choose><html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.scolloca" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.cancInv" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.salva" bundle="documentoFisicoLabels" />
						</html:submit><html:submit styleClass="pulsanti" property="methodVaiAModColl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>


