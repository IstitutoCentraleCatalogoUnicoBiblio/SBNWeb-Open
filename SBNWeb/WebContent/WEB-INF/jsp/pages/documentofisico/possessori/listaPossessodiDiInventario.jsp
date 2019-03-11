<!--	SBNWeb - Rifacimento ClientServer
		JSP di sintetica autori
		Alessandro Segnalini - Inizio Codifica aprile 2008
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/possessori/legamePossessoreInventario.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="documentofisico.legame.Inventariolegame" bundle="documentoFisicoLabels" /></td>
				<td class="testoNormale"><html:text property="codBib" size="5" maxlength="3"
					readonly="true"></html:text></td>
				<td class="testoNormale"><html:text property="codSerie" size="5" maxlength="3"
					readonly="true"></html:text></td>
				<td class="testoNormale"><html:text property="codInv" size="10" maxlength="10"
					readonly="true"></html:text></td>
			</tr>
		</table>
		<hr color="#dde8f0" />
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodPossSintetica"></sbn:blocchi>
		<table class="sintetica">
			<tr bgcolor="#dde8f0">
				<th class="etichetta"><bean:message key="documentofisico.sintetica.PID"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.nome"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.legame"
					bundle="documentoFisicoLabels" /></th>
				<c:choose>
					<c:when test="${possessoriDiInventarioForm.prov != 'DOCFISVIS'}">
						<th class="etichetta"></th>
					</c:when>
				</c:choose>
			</tr>
			<logic:iterate id="item" property="listaPossessori" name="possessoriDiInventarioForm"
				indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td bgcolor="${color}" >
						<sbn:anchor name="item" property="pid" /><sbn:linkbutton index="pid" name="item"
						value="pid" key="sintetica.button.analitica" bundle="documentoFisicoLabels"
						title="analitica" property="selezRadio"
						withAnchor="false" /></td>
					<td bgcolor="${color}" ><bean-struts:write name="item"
						property="nome" /></td>
					<td bgcolor="${color}" ><bean-struts:write name="item"
						property="forma" /></td>
					<c:choose>
						<c:when test="${possessoriDiInventarioForm.prov != 'DOCFISVIS'}">
							<td bgcolor="${color}"><html:radio property="selezRadio" value="${item.pid}" /></td>
						</c:when>
					</c:choose>
				</tr>
			</logic:iterate>
		</table>
		</div>
		<div id="divFooterCommon"><sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="totRighe" totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodPossSintetica" bottom="true"></sbn:blocchi></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodPossDiInventatio">
					<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
				</html:submit></td>
				<c:choose>
					<c:when test="${possessoriDiInventarioForm.prov ne 'DOCFISVIS'}">
						<td align="center"><sbn:checkAttivita idControllo="possessori">
							<html:submit property="methodPossDiInventatio">
								<bean:message key="analitica.button.nuovolegame" bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit property="methodPossDiInventatio">
								<bean:message key="analitica.button.canclegame" bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit property="methodPossDiInventatio">
								<bean:message key="analitica.button.modlegame" bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita></td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
