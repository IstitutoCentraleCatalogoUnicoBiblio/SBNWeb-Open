<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Tipo materiale Moderno/Antico
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

<html:xhtml/>
<layout:page>


	<sbn:navform
		action="/gestionebibliografica/titolo/esaminaTitoliConFiltro.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>
		<table border="0">
			<tr>
				<td class="etichetta"><bean:message key="ricerca.titoloRiferimento"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td width="20" class="testoNormale"><html:text
					property="bidTitRifer" size="10" ></html:text></td>
				<td width="150" class="etichetta"><html:text
					property="descBidTitRifer" size="50" ></html:text></td>
			</tr>
		</table>

		<hr color="#dde8f0" />

		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitCanaliFiltri.jsp" />

		<!--	Segue la parte relativa ai dati specifici dei tipi materiale
           				 diversi da Documento (Musica/Grafica/Cartografia)
           	--> <c:choose>
			<c:when test="${esaminaTitoliConFiltroForm.tipoMateriale eq 'C'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriCartografia.jsp" />
			</c:when>
			<c:when test="${esaminaTitoliConFiltroForm.tipoMateriale eq 'G'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriGrafica.jsp" />
			</c:when>
			<c:when test="${esaminaTitoliConFiltroForm.tipoMateriale eq 'U'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriMusica.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose></div>

		<div id="divFooter"><jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitParametriGen.jsp" />

		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodInterrogTit">
					<bean:message key="ricerca.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>

		</div>

	</sbn:navform>
</layout:page>
