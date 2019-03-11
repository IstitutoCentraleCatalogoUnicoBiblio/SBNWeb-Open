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
	<sbn:navform action="/gestionestampe/bollettino/stampaBollettino.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /><sbn:errors />
		</div>
		<div id="content">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaBollettino">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaBollettinoForm" property="descrBib" /></td>
			</tr>
		</table>
		<BR>
		<table width="50%" align="left" cellpadding="0" cellspacing="0">
			<tr>
				<td><bean:message key="bollettino.dataDa" bundle="gestioneStampeLabels" />&nbsp;&nbsp;
					<html:text styleId="testoNormale" property="dataDa" size="10" maxlength="10">
					</html:text></td>
				<td><bean:message key="bollettino.dataA" bundle="gestioneStampeLabels" />&nbsp;&nbsp;
					<html:text styleId="testoNormale" property="dataA" size="10" maxlength="10">
					</html:text></td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td><bean:message key="bollettino.nuoviTitoli" bundle="gestioneStampeLabels" /><html:radio
					property="check" value="nuoviTitoli" /></td>
				<td><bean:message key="bollettino.nuoviEsemplari" bundle="gestioneStampeLabels" /><html:radio
					property="check" value="nuoviEsemplari" /></td>
			</tr>
		</table>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr><td>
		<BR>
		<div>
				<div id="divFooterCommon">
		<table width="50%"  align="right">
			<tr>
				<td class="etichetta"></td>
				<td class="testoNormale"></td>
				<td class="etichetta"><bean:message key="documentofisico.ordinamento"
					bundle="documentoFisicoLabels" /></td>
				<td class="testoNormale"><html:select property="tipoOrdinamento">
					<html:optionsCollection property="listaTipiOrdinamento" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		</div>
		<HR></div></td></tr>
		</table>

		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaBollettinoForm.disable == false}">
						<td><html:submit styleClass="pulsanti" property="methodStampaBollettino">
							<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodStampaBollettino">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit styleClass="pulsanti" property="methodStampaBollettino">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
