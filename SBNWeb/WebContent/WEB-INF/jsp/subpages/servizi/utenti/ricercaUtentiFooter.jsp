<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<div id="divFooterCommon">
	<table width="100%" border="0">
		<tr>
			<td class="etichetta" width="30%">
				<bean:message key="servizi.label.elementiPerBlocco" bundle="serviziLabels" />
			</td>
			<td>
				<html:text styleId="testoNoBold" property="ricerca.numeroElementiBlocco" size="5"></html:text>
			</td>
			<td class="etichetta"  width="16%">
				<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
			</td>
			<td  class="testoNormale">
				<html:select property="ricerca.ordinamento" >
					<html:optionsCollection property="lstTipiOrdinamento" value="codice" label="descrizione" />
				</html:select>
			</td>
		</tr>
	</table>
</div>
<div id="divFooter">
	<c:choose>
		<c:when test="${navForm.ricerca.parametro eq 'true'}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utenti/footerServizioRicUtenti.jsp" />
		</c:when>
		<c:otherwise>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utenti/footerRicercaUtenti.jsp" />
		</c:otherwise>
	</c:choose>
</div>
