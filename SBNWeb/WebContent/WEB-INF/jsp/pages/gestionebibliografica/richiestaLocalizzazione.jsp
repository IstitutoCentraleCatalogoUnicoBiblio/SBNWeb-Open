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
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>

	<sbn:navform
		action="/gestionebibliografica/utility/richiestaLocalizzazione.do"
		enctype="multipart/form-data">

		<div id="divForm">

			<div id="divMessaggio">
				<sbn:errors />
			</div>

			<br />
			<table>
				<tr>
					<td><bean:message key="label.localizza.massiva.loc.tipoInput"
							bundle="gestioneBibliograficaLabels" /></td>
					<td><bean:message key="label.localizza.massiva.loc.pendenti"
							bundle="gestioneBibliograficaLabels" /></td>
					<td><html:radio property="richiesta.tipoInput" value="DB" />
						&nbsp;<bs:write name="navForm" property="pendingMessage" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>File XML</td>
					<td><html:radio property="richiesta.tipoInput" value="FILE" />&nbsp;
						<bean:message key="label.selezionafile" bundle="esportaLabels" />
						<html:file property="input" />&nbsp; <html:submit
							property="methodRichAllineamenti">
							<bean:message key="button.caricafile" bundle="esportaLabels" />
						</html:submit></td>
				</tr>
			</table>
		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
