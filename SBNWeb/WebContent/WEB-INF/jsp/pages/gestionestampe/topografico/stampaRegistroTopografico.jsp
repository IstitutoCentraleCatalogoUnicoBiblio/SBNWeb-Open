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
	<div id="divMessaggio"><sbn:errors /></div>
	<sbn:navform action="/gestionestampe/topografico/stampaRegistroTopografico.do">
		<div id="divForm">
		<table width="100%" align="center">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaRegistroTopografico">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaRegistroTopograficoForm" property="descrBib" /></td>
			</tr>
		</table>
		<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" /></div>
		<bean-struts:size id="comboSize" name="stampaRegistroTopograficoForm"
			property="elencoModelli" />
		<!-- INIZIO TRATTAMENTO MODELLO -->
		<table width="100%" border="0">
			<tr>
				<logic:greaterEqual name="comboSize" value="2">
					<!--Selezione Modello Via Combo-->
					<td width="15%" scope="col">
					<div align="left" class="etichetta"><bean:message
						key="biblioteche.label.modello" bundle="gestioneStampeLabels" /></div>
					</td>
					<td colspan="5" valign="top" scope="col" align="left"><html:select
						styleClass="testoNormale" property="tipoModello" style="width:205px">
						<html:optionsCollection property="elencoModelli" value="jrxml" label="descrizione" />
					</html:select></td>
				</logic:greaterEqual>
				<logic:lessThan name="comboSize" value="2">
					<!--Selezione Modello Hidden-->
					<td width="15%" scope="col">
					<div align="left" class="etichetta">&nbsp;</div>
					</td>
					<td colspan="5" valign="top" scope="col" align="left">&nbsp; <html:hidden
						property="tipoModello"
						value="${stampaRegistroTopograficoForm.elencoModelli[0].jrxml}" /></td>
				</logic:lessThan>
			</tr>
		</table>
		<!-- FINE TRATTAMENTO MODELLO -->
		<HR>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaRegistroTopograficoForm.disable == false}">
						<td><html:submit property="methodStampaRegistroTopografico">
							<bean:message key="documentofisico.bottone.conferma" bundle="documentoFisicoLabels" />
						</html:submit><html:submit property="methodStampaRegistroTopografico">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaRegistroTopografico">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>