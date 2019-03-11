<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>


<html:xhtml />
<layout:page>
	<div id="divMessaggio"><sbn:errors /></div>
	<sbn:navform action="/gestionestampe/buoni/stampaBuoniCarico.do"
		enctype="multipart/form-data">
		<div id="divForm">
		<table width="100%" align="center">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaBuoniCarico">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaBuoniCaricoForm" property="descrBib" /></td>
			</tr>
		</table>



		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th colspan="5" class="etichetta" width="50%">Dati di estrazione</th>
			</tr>
			<tr height="30">
				<c:choose>
					<c:when test="${stampaBuoniCaricoForm.folder eq 'RangeInv'}">
						<td width="33%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaBuoniCarico"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaBuoniCaricoForm.folder eq 'Fattura'}">
						<td width="33%" class="schedaOn">
						<div align="center">Fattura</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaBuoniCarico"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerFattura"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaBuoniCaricoForm.folder eq 'NumeroBuono'}">
						<td width="33%" class="schedaOn">
						<div align="center">Numero buono di carico</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaBuoniCarico"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerNumBuonoCarico" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${stampaBuoniCaricoForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${stampaBuoniCaricoForm.folder eq 'Fattura'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selFattura.jsp" />
			</c:when>
			<c:when test="${stampaBuoniCaricoForm.folder eq 'NumeroBuono'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selPerNumBuonoCarico.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<!-- FINE TRATTAMENTO MODELLO -->
		<HR>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
		</table>
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaBuoniCaricoForm.disable == false}">
						<td><html:submit property="methodStampaBuoniCarico">
							<bean:message key="documentofisico.bottone.conferma" bundle="documentoFisicoLabels" />
						</html:submit><html:submit property="methodStampaBuoniCarico">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaBuoniCarico">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
