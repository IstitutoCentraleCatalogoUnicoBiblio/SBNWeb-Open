<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/richiestaProroga.do">

		<div id="divForm">
			<div id="divMessaggio"> <sbn:errors bundle="serviziWebMessages" /></div>
			<br>
		</div>

		<tr>
			<th colspan="4" class="etichetta" align="right">
				<c:out value="${RichiestaProrogaForm.bibsel}"> </c:out>-
				<c:out value="${RichiestaProrogaForm.ambiente}"> </c:out>-
				<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
				<c:out value="${RichiestaProrogaForm.utenteCon}"> </c:out>
				<hr>
			</th>
		</tr>

		<table style="margin-top:0"  border="1" >
			<tr>
				<td><em><strong><bean:message key="servizi.documento.data.ricProroga"  bundle="serviziWebLabels" /></strong></em></td>
			    <td><c:out value="${RichiestaProrogaForm.dataRic}"> </c:out></td>

			    <td><em><strong><bean:message key="servizi.documento.data.maxProrogabile"  bundle="serviziWebLabels" /></strong></em></td>
			    <td><c:out value="${RichiestaProrogaForm.dataMaxProrogabile}"> </c:out></td>
			</tr>

			<tr>
			    <td><em><strong><bean:message key="servizi.documento.dataProroga"  bundle="serviziWebLabels" /></strong></em></td>
			    <td>
				    <html:select property="dataMaxProroga" name="RichiestaProrogaForm">
						<html:optionsCollection property="dataPrevRitDoc" name="RichiestaProrogaForm" label="codice" value="codice" />
					</html:select>
				</td>
				<%--<td><html:text name="RichiestaProrogaForm" property="dataMaxProroga" maxlength="10"></html:text>(gg/mm/aaaa)</td> --%>
			</tr>
		</table>

		<tr>
			<td class="etichetta" align="left">
				<hr><bean:message key="servizi.web.richiestaProroga.datiObbligatori"  bundle="serviziWebLabels" /><hr>
			</td>
		</tr>

		<html:submit styleClass="submit" property="paramRchProroga">
			<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
		</html:submit>

		<html:submit styleClass="submit" property="paramRchProroga">
			<bean:message key="servizi.bottone.inserimento.proroga" bundle="serviziWebLabels" />
		</html:submit>

	</sbn:navform>
</layout:page>
