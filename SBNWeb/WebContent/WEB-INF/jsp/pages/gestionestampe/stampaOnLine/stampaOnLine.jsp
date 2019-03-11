<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionestampe/stampaOnLine/StampaOnLine.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<c:choose>
			<c:when test="${StampaOnLineForm.tipoStampa eq 'STAMPA_ETICHETTE'}">
				<hr>
				<table width="100%" border="0">
					<tr>
						<td colspan="2" align="right"><bean:message
							key="documentofisico.etichVuoteInizT" bundle="documentoFisicoLabels" /></td>
						<td><html:text disabled="false" property="etichetteVuoteIniziali" size="10"
							maxlength="9"></html:text></td>
					</tr>
					<tr>
						<td colspan="2" align="right"><bean:message key="documentofisico.numCopieT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text disabled="false" property="numCopie" size="5" maxlength="2"></html:text></td>
					</tr>
					<tr>
						<td width="2%"><html:radio property="modalita" value="modConfig"  onchange="this.form.submit()"/></td>
						<td width="15%"><bean:message key="documentofisico.formatoEtichettaT"
							bundle="documentoFisicoLabels" /></td>
						<td width="83%"><html:text disabled="false" property="tipoModello" size="50"
							maxlength="30"></html:text><html:submit property="methodStampaOnLine">
							<bean:message key="documentofisico.lsModelli" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
					<tr>
						<td width="2%"><html:radio property="modalita" value="modBarCode"  onchange="this.form.submit()"/></td>
						<td colspan="2"><bean:message key="documentofisico.formatoEtichettaBarCodeT"
							bundle="documentoFisicoLabels" /></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${StampaOnLineForm.tipoStampa eq 'STAMPA_RICHIESTA'}">
				<hr>
				<table  border="0">
					<tr>
						<td align="left"><bean:message key="documentofisico.numCopieT"
							bundle="documentoFisicoLabels" /></td>
						<td><html:text disabled="false" property="numCopie" size="5" maxlength="2"></html:text></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${StampaOnLineForm.tipoStampa eq 'STAMPA_ORDINE_RILEGATURA'}">
				<sbn:checkAttivita idControllo="GOOGLE">
					<hr>
					<table  border="0">
						<tr>
							<td align="left">
								<bean:message key="ordine.label.lettera" bundle="gestioneStampeLabels"/></td><td>
								<html:radio property="ordine.tipoStampaOrdine" value="STAMPA_ORDINE_RILEGATURA" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<bean:message key="documentofisico.moduloPrelievo" bundle="documentoFisicoLabels" /></td><td>
								<html:radio property="ordine.tipoStampaOrdine" value="STAMPA_MODULO_PRELIEVO" />
							</td>
							<td width="10%">&nbsp;</td>
							<td><bean:message key="documentofisico.dataPrelievo"
									bundle="documentoFisicoLabels" /> <html:text
									styleId="testoNormale" property="ordine.dataPrelievo" size="10"
									maxlength="10"></html:text>
							</td>
							<td><bean:message key="documentofisico.motivoPrelievo"
									bundle="documentoFisicoLabels" /> <html:text
									styleId="testoNormale" property="ordine.motivoPrelievo" size="30"
									maxlength="30"></html:text>
							</td>
						</tr>
						<tr>
							<td align="left">
								<bean:message key="ordine.label.cartSheetPatronCard" bundle="gestioneStampeLabels"/> </td><td>
								<html:radio property="ordine.tipoStampaOrdine" value="STAMPA_CART_ROUTING_SHEET" />
							</td>
						</tr>
					</table>
				</sbn:checkAttivita>
			</c:when>
			<c:when test="${StampaOnLineForm.tipoStampa eq 'STAMPA_MODULO_PRELIEVO'}">
				<hr>
				<table  border="0">
					<tr>
						<td>
							<bean:message key="documentofisico.motivoPrelievo" bundle="documentoFisicoLabels" />
							<html:text styleId="testoNormale" property="motivoPrelievo" size="30" maxlength="30" />
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<!-- default -->
			</c:otherwise>
		</c:choose> <!-- FINE tabella corpo COLONNA + LARGA -->
		<jsp:include flush="true" page="/WEB-INF/jsp/subpages/gestionestampe/stampaOnlineSelectModelli.jsp" />
		</div>
		<hr>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<hr>
		<br>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<c:choose>
				<c:when test="${StampaOnLineForm.tipoStampa eq 'STAMPA_RICHIESTA_AVANZA'}">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodStampaOnLine">
							<bean:message key="servizi.bottone.si" bundle="serviziLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodStampaOnLine">
							<bean:message key="servizi.bottone.no" bundle="serviziLabels" />
						</html:submit></td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodStampaOnLine">
							<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodStampaOnLine">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</tr>
				</c:otherwise>
			</c:choose>

		</table>
		</div>
	</sbn:navform>
</layout:page>