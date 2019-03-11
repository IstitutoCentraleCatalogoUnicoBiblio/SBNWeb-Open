<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/soggetto/GestioneDescrittore.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<html:hidden property="codice" /> <!-- Intestazione -->
		<table width="100%" border="0">
			<tr>
				<td><c:choose>
					<c:when test="${navForm.dettDesGenVO.livelloPolo}">
						<bean:message key="label.livRicercaPolo"
							bundle="gestioneBibliograficaLabels" />
					</c:when>
					<c:otherwise>
						<bean:message key="label.livRicercaIndice"
							bundle="gestioneBibliograficaLabels" />
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td class="etichetta" scope="col"><bean:message
					key="esamina.did" bundle="gestioneSemanticaLabels" />&nbsp;<html:text property="did" readonly="true">
				</html:text></td>

				<td class="etichetta">&nbsp;<bean:message key="esamina.soggettario"
					bundle="gestioneSemanticaLabels" />&nbsp;<html:select styleClass="testoNormale"
					property="ricercaComune.codSoggettario" disabled="true">
					<html:optionsCollection property="listaSoggettari" value="codice"
						label="descrizione" />
				</html:select></td>
				<c:if test="${navForm.dettDesGenVO.gestisceEdizione}">
					<td>
						<bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" />&nbsp;
							<html:select styleClass="testoNormale" property="dettDesGenVO.edizioneSoggettario"
								disabled="${navForm.enableConferma or not navForm.enableManuale}">
							<html:optionsCollection property="listaEdizioni"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
				</c:if>
				<td class="etichetta">&nbsp;<bean:message key="esamina.statoControllo"
					bundle="gestioneSemanticaLabels" />&nbsp;<html:select styleClass="testoNormale"
					property="dettDesGenVO.livAut">
					<html:optionsCollection property="listaLivelloAutorita" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="gestione.testoDescrittore" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${navForm.enableManuale}">
						<td><html:textarea styleId="testoNormale"
							property="descrittore" cols="90" rows="6"
							disabled="${navForm.enableConferma}" /> <sbn:tastiera
							limit="240" name="navForm" property="descrittore"
							visible="${!navForm.enableConferma}" /></td>
					</c:when>
					<c:otherwise>
						<td><html:textarea styleId="testoNormale"
							property="descrittore" cols="90" rows="6" readonly="true"></html:textarea></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale" property="note"
					cols="90" rows="6"
					disabled="${navForm.enableConferma}" /> <sbn:tastiera
					limit="240" visible="${!navForm.enableConferma}"
					name="navForm" property="note" /></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="gestione.formaNome" bundle="gestioneSemanticaLabels" /> <html:select
					styleClass="testoNormale" property="dettDesGenVO.formaNome" disabled="true">
					<html:optionsCollection property="listaFormaNome" value="codice"
						label="descrizione" />
				</html:select>&nbsp;<bean:message
					key="esamina.categoria.did" bundle="gestioneSemanticaLabels" />&nbsp;
					<html:select styleClass="testoNormale" property="dettDesGenVO.categoriaTermine" disabled="${navForm.enableConferma}">
							<html:optionsCollection property="listaCategoriaTermine"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="left" class="etichetta" scope="col"><bean:message
					key="gestione.inserito" bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="gestione.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="dataInserimento" size="14" maxlength="20" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="gestione.modificato"
					bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="gestione.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale" property="dataModifica"
					size="14" maxlength="20" readonly="true"></html:text></td>
			</tr>
		</table>

		</div>
		<div id="divFooter"><c:choose>
			<c:when test="${navForm.enableConferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/bottonieraGestioneDescrittore.jsp" />
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
