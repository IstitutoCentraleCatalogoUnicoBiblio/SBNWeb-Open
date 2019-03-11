<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<layout:page>
	<sbn:navform action="/documentofisico/parametriBiblio/parametriBiblio.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" width="10%"><bean:message
					key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />
				<html:text disabled="true" styleId="testoNormale" property="codBib"
					size="5" maxlength="3"></html:text> <html:submit
					  title="Lista Biblioteche" styleClass="buttonImageListaSezione" disabled="false" property="methodParBiblio">
					<bean:message key="documentofisico.lsBib"
						bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="parametriBiblioForm" property="descrBib" /></td>
			</tr>
		</table>
		<br><!--
		<table width="100%" border="0"
			style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
			<tr>
				<td width="21%" class="etichetta"><bean:message
					key="documentofisico.scaricoUnimarcT" bundle="documentoFisicoLabels" /></td>
				<td width="24%"><html:text disabled="false"
					styleId="testoNormale" property="codScarUni" size="10"
					maxlength="10"></html:text> <html:submit property="methodParBiblio">
					<bean:message key="documentofisico.lsCodUni"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
				<td width="51%">&nbsp;</td>
			</tr>
		</table>

		-->
		<%-- <table border="0" width="100%">
				<tr>
					<td class="testo">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" class="testo"><bean:message
						key="documentofisico.etichetteElenco" bundle="documentoFisicoLabels" /><span class="testo">&nbsp;
					<html:select property="codScaricoSelez" style="width:200px">
						<html:optionsCollection property="listaCodScarico"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
				</tr>
				<tr>
					<td class="testo">&nbsp;</td>
				</tr>
		</table>--%>
		<br>
		<!--
		folders
 -->
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="30">
				<c:choose>
					<c:when test="${parametriBiblioForm.folder == 'ParStEtich'}">
						<td width="50%" class="schedaOn">
						<div align="center">Parametri Serie / Stampa Etichette</div>
						</td>
					</c:when>
					<c:otherwise>
						<td>
						<td width="50%" class="schedaOff">
						<div align="center"><html:submit property="methodParBiblio"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selParStEtich"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${parametriBiblioForm.folder eq 'ParStSchede'}">
						<td width="50%" class="schedaOn">
						<div align="center">Parametri Stampa Schede</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="50%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodParBiblio" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selParStSchede"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<!--
		richiamo subpages
 --> <c:choose>
			<c:when test="${parametriBiblioForm.folder eq 'ParStEtich'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/parametriBiblio/parametriBiblioParStEtich.jsp" />
			</c:when>
			<c:when test="${parametriBiblioForm.folder eq 'ParStSchede'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/parametriBiblio/parametriBiblioParStSchede.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose></div>

		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<td><html:submit property="methodParBiblio">
					<bean:message key="documentofisico.bottone.ok"
						bundle="documentoFisicoLabels" />
				</html:submit> </td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>


