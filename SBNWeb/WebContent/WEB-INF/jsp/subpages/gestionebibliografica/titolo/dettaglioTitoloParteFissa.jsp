<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area superiore con canali e filtri
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<c:choose>
	<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'DET'}">
		<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaDETTAGLIO.jsp" />
	</c:when>

	<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'VARIANAT'}">
		<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIANAT.jsp" />
	</c:when>

	<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'AGGNOTA'}">
		<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaAGGNOTA.jsp" />
	</c:when>

	<c:otherwise>
		<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONE.jsp" />
	</c:otherwise>
</c:choose>


<table border="0">
	<tr>
		<td width="80" class="etichetta"><bean:message
			key="ricerca.dataInserimento" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:text
			property="dettTitComVO.detTitoloPFissaVO.dataIns" size="10"
			readonly="true"></html:text></td>

		<td width="120" class="etichetta"><bean:message
			key="ricerca.dataUltimoAgg" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:text
			property="dettTitComVO.detTitoloPFissaVO.dataAgg" size="10"
			readonly="true"></html:text></td>
	</tr>
</table>
