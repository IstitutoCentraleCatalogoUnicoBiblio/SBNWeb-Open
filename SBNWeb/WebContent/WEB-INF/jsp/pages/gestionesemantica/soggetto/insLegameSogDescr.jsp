<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/InsLegameSogDescr.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors />
		</div> <!-- Intestazione -->
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
		<table width="100%" border="0">
			<tr>
				<td class="etichetta">Base dati in aggiornamento Polo</td>
			</tr>
		</table>
		<table width="240em" border="0">
			<tr>
				<td class="etichetta" scope="col"><bean:message key="gestione.did"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text property="did"
					readonly="true">
				</html:text></td>
				<td class="etichetta"><bean:message key="sintetica.soggettario"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="ricercaComune.codSoggettario" disabled="true">
					<html:optionsCollection property="listaSoggettari" value="codice"
						label="descrizione" />
				</html:select>
				</td>
				<c:if test="${navForm.dettSogGenVO.gestisceEdizione}">
					<td >&nbsp;</td>
					<td><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" /></td>
					<td><html:select styleClass="testoNormale"
							property="dettSogGenVO.edizioneSoggettario" disabled="true">
							<html:optionsCollection property="listaEdizioni"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</td>
				</c:if>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="gestione.testo" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale" property="descrittore"
					cols="90" rows="6"></html:textarea>
					<sbn:tastiera limit="240" name="InsLegameSogDescrForm" property="descrittore"/></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale" property="note" cols="90"
					rows="6"></html:textarea>
					<sbn:tastiera limit="240" name="InsLegameSogDescrForm" property="note"/>
				</td>
			</tr>
		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodInsSogDes">
					<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodInsSogDes">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodInsSogDes">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
