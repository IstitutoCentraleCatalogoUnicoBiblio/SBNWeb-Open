<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
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
		action="/gestionebibliografica/autore/dettaglioLegameAutoreMarca.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>
		 <jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/autore/dettaglioDatiLegameAutoreMar.jsp" />

		<hr color="#dde8f0" />

		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/marca/dettaglioMarcaDati.jsp" />

		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodDettLegAutMar">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
