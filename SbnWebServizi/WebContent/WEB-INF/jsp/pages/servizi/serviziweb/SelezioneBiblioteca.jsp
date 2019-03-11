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
	<sbn:navform action="/serviziweb/selezionaBib.do" >
	<table cellspacing="0" width="100%" border="0">
			<tr>
				<td >
					<div id="divMessaggio"><sbn:errors bundle="serviziWebMessages" /></div>
				</td>
			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="right">
				<c:out value="${selezioneBibliotecaForm.ambiente}"> </c:out> -
				<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
					<c:out value="${selezioneBibliotecaForm.utenteCon}"> </c:out>
				<hr>
				</th>
			</tr>
			<c:if test="${not empty selezioneBibliotecaForm.listaBiblio}">
			<tr>
				<td align="left">

						<bean:message key="servizi.utenti.selBiblio" bundle="serviziWebLabels" />
						<html:select styleClass="testoNormale" property="biblio.cod_bib">
							<html:optionsCollection property="listaBiblio" value="cod_bib" label="nom_biblioteca" />
						</html:select>
						<html:submit styleClass="submit" property="paramSelBib">
							<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
						</html:submit>



				</td>
			</tr>
			</c:if>
			<c:if test="${not empty selezioneBibliotecaForm.listaBiblioAuto}">
	       		<tr>
		       		<td align="left">

						<bean:message key="servizi.utenti.iscrizioneBiblio" bundle="serviziWebLabels" />

						<html:select styleClass="testoNormale" property="biblioAuto.prg">
							<html:optionsCollection property="listaBiblioAuto" value="prg"  label="nom_biblioteca" />
						</html:select>

						<html:submit styleClass="submit" property="paramSelBib">
							<bean:message key="servizi.bottone.iscrizione" bundle="serviziWebLabels" />
						</html:submit>


					</td>
				</tr>
			</c:if>

		</table>
	</sbn:navform>
</div>
</layout:page>