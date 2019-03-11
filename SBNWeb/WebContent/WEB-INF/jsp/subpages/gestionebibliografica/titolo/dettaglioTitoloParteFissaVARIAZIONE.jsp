<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaVARIAZIONE
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


		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.bid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.bid" size="10"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<c:choose>
			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'A'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatA.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'V'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatV.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'D'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatBDP.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'P'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatBDP.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'B'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatBDP.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'T'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatT.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'R'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatR.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatM.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatN.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'C'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatC.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatS.jsp" />
			</c:when>

			<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'}">
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaVARIAZIONEnatW.jsp" />
			</c:when>
		</c:choose>