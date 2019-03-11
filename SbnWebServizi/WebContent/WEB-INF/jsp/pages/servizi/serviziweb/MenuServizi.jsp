<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<div id="header"></div>
	<div id="data">
		<sbn:navform action="/serviziweb/menuServizi.do" >
		<table cellspacing="0" width="100%" border="0">
				<tr>
					<td >
						<div id="divMessaggio"><sbn:errors bundle="serviziWebMessages" /></div>
					</td>
				</tr>
				<tr>
					<th colspan="4" class="etichetta" align="right">
						<c:out value="${menuServiziForm.biblioSel}"> </c:out>-
						<c:out value="${menuServiziForm.ambiente}"> </c:out> -
						<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
						<c:out value="${menuServiziForm.utenteCon}"> </c:out>
					<hr>
					</th>
				</tr>
				<tr>
					<th colspan="4" class="etichetta">
						<bean:message key="servizi.utenti.titoloMenuServizi" bundle="serviziWebLabels" />
					</th>
				</tr>

				<tr>
					<td >
						&nbsp;
					</td>
				</tr>
				<tr>
					<td  class="etichetta" align="center">
						<bean:message key="servizi.utenti.insSegnatura"
						bundle="serviziWebLabels" />

						<html:text property="segnatura"
								styleId="testoNormale" size="30" maxlength="30"></html:text>

						<html:submit styleClass="submit" property="paramMenuServ" >
							<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
						</html:submit>

					<hr>
					</td>
				</tr>
				<sbn:checkAttivita idControllo="PRENOTA_POSTO">
				<tr>
					<td  class="etichetta" align="center">
						<html:link page="/serviziweb/gestionePrenotazionePosto.do">
							<bean:message key="servizi.utenti.prenotaPosto"	bundle="serviziWebLabels" />
						</html:link>
					<hr>
					</td>
				</tr>
				</sbn:checkAttivita>
				<!--
				<tr>
					<td>
					Utilizzare i <html:link page="/serviziweb/serviziILL.do">Servizi ILL</html:link> per una <b>richiesta di documenti dalla tua biblioteca
					 ad altre biblioteche</b>
					 <hr>
					</td>
				</tr>
				-->
				<!--
				<tr>
					<td>
						<html:link page="/serviziweb/datiDocumento.do">Nuovo suggerimento di acquisto</html:link>
					</td>
				</tr>
				-->

			</table>
		</sbn:navform>
	</div>
</layout:page>