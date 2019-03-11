<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

<div id="header"></div>
<div id="data">
	<sbn:navform action="/serviziweb/sceltaServizi.do" >
	<table cellspacing="0" width="100%" border="0">

			<tr>
				<th colspan="4" class="etichetta" align="right">
					<c:out value="${menuServiziForm.biblioSel}"> </c:out>
				<hr>
				</th>
			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="right">
				<bean:message key="servizi.documento.poloEserWeb"
					bundle="serviziWebLabels" /> -
				<bean:message key="servizi.utenti.utenteConn"
					bundle="serviziWebLabels" />

					<c:out value="${selezioneBibliotecaForm.utenteCon}"> </c:out>
				<hr>
				</th>
			</tr>



			<tr>
				<th colspan="4" class="etichetta">
					<bean:message key="servizi.documento.titoloSceltaServ"
					bundle="serviziWebLabels" />

				</th>
			</tr>


			<tr>
				<td colspan="4" align="center">
				<bean:message key="servizi.documento.selServ"
					bundle="serviziWebLabels" /><br>
					- <b><c:out value="${sceltaServiziForm.titolo}"> </c:out></b>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="left">
					<b><bean:message key="servizi.documento.diritti"
					bundle="serviziWebLabels" /></b>
					<br>
				</td>
			</tr>
			<logic:iterate id="item" property="listaServ" name="sceltaServiziForm" indexId="riga">
				<tr>

					<td colspan="4" align="left">

						<html:link page="/serviziweb/serviziDisponibili.do?servizio=${item.idServizio}"> <c:out value="${item.servizio}"></c:out></html:link>
					</td>
				</tr>
			</logic:iterate>
			<tr>
				<td colspan="4" align="center">
					<sbn:errors />
				</td>
			</tr>
			<tr>
				<td >
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
				<bean:message key="servizi.utente.elencoDirittiDoc"
					bundle="serviziWebLabels" /> <html:link page="/serviziweb/dirittiDocumento.do"><bean:message key="servizi.utente.elencoDirittiDocLink"
					bundle="serviziWebLabels" /> </html:link>
				 <hr>
				</td>
			</tr>
			<tr>
				<td  class="etichetta" align="left">

					<html:submit styleClass="submit" property="paramSceltaServ" >
						<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
					</html:submit>

				<hr>
				</td>
			</tr>
	</table>
	</sbn:navform>
</div>
</layout:page>