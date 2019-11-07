<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<table width="100%" border="0">
	<c:choose>
		<c:when
			test="${navForm.class.simpleName eq 'EsameCollocRicercaForm'}">
			<tr>
				<td>
					<div align="left" class="etichetta">
						<bean:message key="documentofisico.serieT"
							bundle="documentoFisicoLabels" />
					</div>
				</td>
				<td>
				<!-- inventario singolo -->
				<c:choose>
						<c:when test="${currentForm.noSerie}">
							<html:text disabled="false" styleId="testoNormale"
								property="codSerie" size="3" maxlength="3"></html:text>
						</c:when>
						<c:otherwise>
							<html:select property="codSerie">
								<html:optionsCollection property="listaComboSerie"
									value="codice" label="codice" />
							</html:select>
						</c:otherwise>
					</c:choose> <html:text disabled="false" styleId="testoNormale"
						property="codInvent" size="10" maxlength="9"></html:text> <sbn:checkAttivita
						idControllo="RFID">
					&nbsp;(<bean:message key="documentofisico.rfid"
							bundle="documentoFisicoLabels" />
						<html:text disabled="false" styleId="testoNormale"
							property="codRfid" size="18" onkeydown="submitOnEnter(event, 'btnCerca')" />)
					</sbn:checkAttivita>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr class="etichetta">
				<td colspan="4">
					<table width="60%">
						<tr>
							<!-- range inventari -->
							<td><bean:message key="documentofisico.serieT"
									bundle="documentoFisicoLabels" /></td>
							<td><html:select property="serie"
									disabled="${currentForm.disableSerie}">
									<html:optionsCollection property="listaComboSerie"
										value="codice" label="codice" />
								</html:select></td>
							<td><bean:message key="documentofisico.dalNumero"
									bundle="documentoFisicoLabels" /> <html:text
									styleId="testoNormale" property="startInventario"
									disabled="${currentForm.disableDalNum}" size="10"
									maxlength="9"></html:text></td>
							<td><bean:message key="documentofisico.alNumero"
									bundle="documentoFisicoLabels" /> <html:text
									styleId="testoNormale" disabled="${currentForm.disableAlNum}"
									property="endInventario" size="10" maxlength="9"></html:text>
							</td>
						</tr>
					</table>
			</tr>
			<c:choose>
				<c:when
					test="${navForm.class.simpleName eq 'StampaBuoniCaricoForm'}">
					<BR>
					<tr>
						<td><bean:message key="documentofisico.ristampaBuonoCarico"
								bundle="documentoFisicoLabels" /> <html:checkbox
								property="ristampaDaInv"></html:checkbox> <html:hidden
								property="ristampaDaInv" value="false" />
						</td>
					</tr>
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<BR>
</table>
