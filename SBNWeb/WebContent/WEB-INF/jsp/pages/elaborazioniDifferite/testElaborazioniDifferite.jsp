<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>



<html:xhtml />
<layout:page>
	<sbn:navform
		action="/elaborazioniDifferite/testElaborazioniDifferite.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

		<bean:message key="title.test.testElaborazioniDifferite"
			bundle="elaborazioniDifferiteLabels" />


		<table class="sintetica">

			<tr bgcolor="#dde8f0">
				<td class="etichetta" align="center">#</td>
				<td class="etichetta" align="center"><bean:message
					key="label.test.procedura" bundle="elaborazioniDifferiteLabels" /></td>
				<td class="etichetta" align="center"><bean:message
					key="label.test.codaAssegnata" bundle="elaborazioniDifferiteLabels" /></td>
				<td class="etichetta" align="center"><bean:message
					key="label.test.idRichiestaAssegnato"
					bundle="elaborazioniDifferiteLabels" /></td>
				</td>
			</tr>



			<c:set var="color">
				<c:choose>
					<c:when test='${color == "#FFCC99"}'>
					            #FEF1E2
					        </c:when>
					<c:otherwise>
								#FFCC99
					        </c:otherwise>
				</c:choose>
			</c:set>

			<tr class="testoNormale" bgcolor="${color}">

				<td align="center">--</td>
				<td align="center"><bean-struts:write
					name="testElaborazioniDifferiteForm" property="procedura" /></td>
				<td align="center"><bean-struts:write
					name="testElaborazioniDifferiteForm" property="codaAssegnata" /></td>
				<td align="center"><bean-struts:write
					name="testElaborazioniDifferiteForm"
					property="idRichiestaAssegnato" /></td>
			</tr>


		</table>
		</div>


		<div id="divFooter">
		<table align="center" border="0" style="height: 40px">
			<tr>
				<td align="center"><html:submit styleClass="pulsanti"
					property="methodMap_testElaborazioniDifferite">
					<bean:message key="button.indietro"
						bundle="elaborazioniDifferiteLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>






	</sbn:navform>
</layout:page>
