<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/soggetto/PrenotaElaborazioneDifferita.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<br/>
		<table width="100%" border="0">
			<tr>
				<td width="15%" valign="top" scope="col">
				<div align="left" class="etichetta"><bean:message
					key="soggettario.label.codSogg" bundle="gestioneStampeLabels" /></div>
				</td>
				<td valign="top" scope="col">
				<div align="left"><html:select styleClass="testoNormale"
					property="parametri.codSoggettario" disabled="${prenotaBatchSemanticaForm.conferma}">
					<html:optionsCollection property="listaSoggettari" value="codice"
						label="descrizione" />
				</html:select></div>
				</td>
				<td>
					<div align="left" class="etichetta"><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" />&nbsp;
						<html:select styleClass="testoNormale" property="parametri.edizioneSoggettario">
							<html:optionsCollection property="listaEdizioni" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</div>
				</td>
			</tr>
		</table>
		</div>
		<br/>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="listaPulsanti" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
