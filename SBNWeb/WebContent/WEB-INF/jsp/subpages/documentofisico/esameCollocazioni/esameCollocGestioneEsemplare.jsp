<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>

<div id="divForm">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
<!--  biblioteca -->
<table width="100%" border="0">
	<tr>
		<td colspan="3">
		<div class="etichetta"><bean:message
			key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /> <html:text
			readonly="true" styleId="testoNormale" property="codBib" size="5"
			maxlength="3"></html:text> <bean-struts:write
			name="esameCollocGestioneEsemplareForm" property="descrBib" /></div>
		</td>
	</tr>
</table>
<table width="100%"
	style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
	<tr>
		<td width="18%"><bean:message key="documentofisico.bidCollocazioneT"
			bundle="documentoFisicoLabels" /></td>
		<td><bean-struts:write name="esameCollocGestioneEsemplareForm"
			property="recColl.bid" />&nbsp;&nbsp;&nbsp;<bean-struts:write
			name="esameCollocGestioneEsemplareForm" property="recColl.bidDescr" /></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.bidDocT"
			bundle="documentoFisicoLabels" /></td>
		<td><bean-struts:write name="esameCollocGestioneEsemplareForm"
			property="bidDoc" />&nbsp;&nbsp;&nbsp;<bean-struts:write
			name="esameCollocGestioneEsemplareForm" property="bidDocDescr" /></td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<td width="18%" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="documentofisico.consistenzaEsemplareT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td width="82%" scope="col">
		<div align="left" class="testoNormale"><html:textarea
			styleId="testoNormale" rows="5" cols="70"
			disabled="${esameCollocGestioneEsemplareForm.disable}"
			property="consistenzaEsemplare"></html:textarea></div>
		</td>
	</tr>
</table>
</div>
<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
<table align="center">
	<tr>
		<c:choose>
			<c:when test="${esameCollocGestioneEsemplareForm.conferma}">
				<td><html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
					<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
				</html:submit> <html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
					<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
				</html:submit></td>
			</c:when>
			<c:otherwise>
				<sbn:checkAttivita idControllo="FASCICOLI">
					<td><html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.fascicoli"
								bundle="documentoFisicoLabels" />
						</html:submit></td>
				</sbn:checkAttivita>
				<c:choose>
					<c:when test="${esameCollocGestioneEsemplareForm.esamina}">
						<td><!--<html:submit styleClass="pulsanti"
							property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.indice"
								bundle="documentoFisicoLabels" />
						</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
						<bean:message key="documentofisico.bottone.salva"
									bundle="documentoFisicoLabels" /></html:submit>
					--><html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when test="${esameCollocGestioneEsemplareForm.nuovo}">
						<td>
						<c:choose>
							<c:when test="${esameCollocGestioneEsemplareForm.abilitaBottoneInviaInIndice}">
								<html:submit styleClass="pulsanti" disabled="false"
									property="methodEsameCollGestEsempl">
									<bean:message key="documentofisico.bottone.indice" bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
						</c:choose><html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.salva" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.cambiaEsempl"
								bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.cancEsempl"
								bundle="documentoFisicoLabels" />
						</html:submit><c:choose>
							<c:when test="${esameCollocGestioneEsemplareForm.abilitaBottoneInviaInIndice}">
								<html:submit styleClass="pulsanti" disabled="false"
									property="methodEsameCollGestEsempl">
									<bean:message key="documentofisico.bottone.indice" bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
						</c:choose> <html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.salva" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodEsameCollGestEsempl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
</div>
