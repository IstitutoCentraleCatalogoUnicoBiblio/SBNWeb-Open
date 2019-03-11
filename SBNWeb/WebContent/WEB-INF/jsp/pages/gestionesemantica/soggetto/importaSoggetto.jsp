
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/ImportaSoggetto.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<logic:notEmpty name="navForm"
			property="listaStatoControllo">
			<logic:notEmpty name="navForm"
				property="listaTipoSoggetto">
				<c:if test="${navForm.enableTit}">
					<table width="100%" border="0">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
					</table>
				</c:if>
				<table width="100%" border="0">
					<tr>
						<td class="etichetta"><bean:message
							key="label.livRicercaIndice" bundle="gestioneBibliograficaLabels" /></td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td class="etichetta" scope="col" align="center"><bean:message
							key="gestione.cid" bundle="gestioneSemanticaLabels" /></td>
						<td><html:text styleId="testoNormale" property="dettSogGenVO.cid"
							readonly="true">
						</html:text></td>
						<td class="etichetta"><bean:message
							key="gestione.soggettario" bundle="gestioneSemanticaLabels" /></td>
						<td><html:select styleClass="testoNormale"
							property="dettSogGenVO.campoSoggettario" disabled="true">
							<html:optionsCollection property="listaSoggettari" value="codice"
								label="descrizione" />
						</html:select></td>
					</tr>
					<c:if test="${navForm.dettSogGenVO.gestisceEdizione}">
						<tr>
							<td colspan="2">&nbsp;</td>
							<td><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" /></td>
							<td><html:select styleClass="testoNormale"
									property="dettSogGenVO.edizioneSoggettario">
									<html:optionsCollection property="listaEdizioni"
										value="cd_tabellaTrim" label="ds_tabella" />
								</html:select></td>
						</tr>
					</c:if>
					<tr>
						<td class="etichetta"><bean:message
							key="gestione.statoDiControllo" bundle="gestioneSemanticaLabels" /></td>
						<td><html:select styleClass="testoNormale"
							property="dettSogGenVO.livAut">
							<html:optionsCollection property="listaStatoControllo"
								value="codice" label="descrizione" />
						</html:select></td>
						<td class="etichetta"><bean:message
							key="gestione.tipoDiSoggetto" bundle="gestioneSemanticaLabels" /></td>
						<td><html:select styleClass="testoNormale"
							property="dettSogGenVO.categoriaSoggetto">
							<html:optionsCollection property="listaTipoSoggetto"
								value="codice" label="descrizione" />
						</html:select></td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td align="center" class="etichetta"><bean:message
							key="gestione.testo" bundle="gestioneSemanticaLabels" /></td>
					</tr>
					<tr>
						<td><html:textarea styleId="testoNormale" property="testo"
							cols="90" rows="6" disabled="true" /> </td>
					</tr>
					<tr>
						<td class="etichetta" align="center"><bean:message
							key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
					</tr>
					<tr>
						<td><html:textarea styleId="testoNormale" property="dettSogGenVO.note"
							cols="90" rows="6"></html:textarea> <sbn:tastiera limit="240"
							name="navForm" property="note" /></td>
					</tr>
				</table>

				<table width="100%" border="0">
					<tr>
						<td align="left" class="etichetta" scope="col"><bean:message
							key="crea.inserito" bundle="gestioneSemanticaLabels" /></td>
						<td class="etichetta"><bean:message key="crea.il"
							bundle="gestioneSemanticaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettSogGenVO.dataIns" size="14" maxlength="20"
							readonly="true"></html:text></td>
						<td class="etichetta"><bean:message key="crea.modificato"
							bundle="gestioneSemanticaLabels" /></td>
						<td class="etichetta"><bean:message key="crea.il"
							bundle="gestioneSemanticaLabels" /></td>
						<td><html:text styleId="testoNormale" property="dettSogGenVO.dataAgg"
							size="14" maxlength="20" readonly="true"></html:text></td>
					</tr>
				</table>
				<div id="divFooter">
				<table align="center">
					<tr>
						<td align="center"><html:submit property="methodImpSog">
							<bean:message key="button.importa"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						<td align="center"><html:submit property="methodImpSog">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</tr>
				</table>
				</div>
			</logic:notEmpty>
		</logic:notEmpty></div>
	</sbn:navform>
</layout:page>

