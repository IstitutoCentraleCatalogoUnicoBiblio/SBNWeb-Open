<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/soggetto/InsDescrPerLegameDescr.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>

			<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />

			<table border="0">
				<tr>
					<td class="etichetta" scope="col"><bean:message
							key="gestione.did" bundle="gestioneSemanticaLabels" />&nbsp;<html:text
							property="did" readonly="true" size="11" /></td>
					<td>&nbsp;<bean:message key="esamina.soggettario"
							bundle="gestioneSemanticaLabels" />&nbsp;<html:select
							styleClass="testoNormale" property="ricercaComune.codSoggettario"
							disabled="true">
							<html:optionsCollection property="listaSoggettari" value="codice"
								label="descrizione" />
						</html:select></td>
						<td>
							<bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" />
							&nbsp;<html:select styleClass="testoNormale" property="ricercaComune.edizioneSoggettario">
								<html:optionsCollection property="listaEdizioni" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</td>
					<td class="etichetta">&nbsp;<bean:message
							key="esamina.statoControllo" bundle="gestioneSemanticaLabels" />&nbsp;
						<sbn:disableAll checkAttivita="CREA">
							<html:select styleClass="testoNormale" property="livAut">
								<html:optionsCollection property="listaLivelloAutorita"
									value="codice" label="descrizione" />
							</html:select>
						</sbn:disableAll></td>
				</tr>
			</table>
			<table width="100%" border="0">
				<tr>
					<td align="center" class="etichetta"><bean:message
							key="gestione.testo" bundle="gestioneSemanticaLabels" /></td>
				</tr>
				<tr>
					<td><html:textarea styleId="testoNormale"
							property="descrittore" cols="90" rows="6"></html:textarea> <sbn:tastiera
							limit="240" name="navForm" property="descrittore" />
				</tr>
				<tr>
					<td class="etichetta" align="center"><bean:message
							key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
				</tr>
				<tr>
					<td><html:textarea styleId="testoNormale" property="note"
							cols="90" rows="6"></html:textarea> <sbn:tastiera limit="240"
							name="navForm" property="note" /></td>
				</tr>
				<tr>
					<td class="etichetta" align="center"><bean:message
							key="gestione.formaNome" bundle="gestioneSemanticaLabels" /> <html:select
							styleClass="testoNormale" property="ricercaComune.formaNome">
							<html:optionsCollection property="listaFormaNome" value="codice"
								label="descrizione" />
						</html:select></td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center"><html:submit property="methodInsDesDes">
							<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					<sbn:checkAttivita idControllo="CREA">
						<logic:equal name="navForm" property="enableCrea" value="true">
							<td align="center"><html:submit property="methodInsDesDes">
									<bean:message key="button.crea"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
						</logic:equal>
					</sbn:checkAttivita>
					<td align="center"><html:submit property="methodInsDesDes">
							<bean:message key="button.stampa"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					<td align="center"><html:submit property="methodInsDesDes">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
