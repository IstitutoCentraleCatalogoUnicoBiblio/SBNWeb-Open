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
	<sbn:navform action="/documentofisico/esameCollocazioni/esameCollocRicerca.do">
		<html:hidden property="action" />
		<html:hidden property="test" />
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="4">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text> <span disabled="true"> <html:submit
					title="Lista Biblioteche" styleClass="buttonImageListaSezione" disabled="false"
					property="methodEsameCollRicerca">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit></span> <bean-struts:write name="navForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>

		<BR>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th colspan="5" class="etichetta" width="50%">Dati di ricerca</th>
			</tr>
			<tr height="30">
				<c:choose>
					<c:when test="${navForm.folder eq 'Collocazioni'}">
						<td width="50%" class="schedaOn">
						<div align="center">Collocazione</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="50%" class="schedaOff">
						<div align="center"><html:submit property="methodEsameCollRicerca"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerCollocazione"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${navForm.folder eq 'RangeInv'}">
						<td width="50%" class="schedaOn">
						<div align="center">Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="50%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodEsameCollRicerca" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerInventari" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${navForm.folder eq 'Collocazioni'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		</table>
		</div>
		<div id="divFooterCommon">
		<table width="100%">
			<tr>
				<td class="etichetta"><bean:message key="documentofisico.elementiBlocco"
					bundle="documentoFisicoLabels" /></td>
				<td class="testoNormale"><html:text property="elemPerBlocchi" size="5"></html:text></td>
				<td class="etichetta"><bean:message key="documentofisico.ordinamento"
					bundle="documentoFisicoLabels" /></td>
				<td class="testoNormale"><html:select property="tipoOrdinamento">
					<html:optionsCollection property="listaTipiOrdinamento" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<td><c:choose>
			<c:when test="${navForm.folder eq 'RangeInv'}">
						<html:submit property="methodEsameCollRicerca">
							<bean:message key="documentofisico.bottone.invNoncolloc"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> <html:submit property="methodEsameCollRicerca" styleId="btnCerca">
					<bean:message key="documentofisico.bottone.cerca" bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
