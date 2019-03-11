<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<layout:page>
	<sbn:navform action="/gestionesemantica/thesauro/gestioneLegameTermineClasse.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" flush="true" />
			<sbn:disableAll disabled="${navForm.conferma}">
			<!--
			<table width="100%" border="0">
				<tr>
					<td align="center" class="etichetta">
						<bean:message key="crea.notaAlLegameThCl"
							bundle="gestioneSemanticaLabels" />
					</td>
				</tr>
				<tr>
					<td>
						<html:textarea styleId="testoNormale" property="legame.notaLegame"
							cols="90" rows="6"></html:textarea>
						<sbn:tastiera limit="90" property="legame.notaLegame"
							name="navForm" />
					</td>
				</tr>
			</table>
			 -->
			<table width="100%" border="0">
				<tr>
					<td class="etichetta">
						<bean:message key="soggettazione.sistema"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:select styleClass="testoNormale"
							property="legame.campoSistema"
							disabled="true">
							<html:optionsCollection property="listaSistemiClassificazione"
								value="cd_tabellaTrim" label="cd_tabella" />
						</html:select>
					</td>
					<td class="etichetta">
						<bean:message key="ricerca.edizione"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:select styleClass="testoNormale"
							property="legame.campoEdizione" disabled="true">
							<html:optionsCollection property="listaEdizioni"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</td>
					<td class="etichetta">
						<bean:message key="crea.statoDiControllo"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:select styleClass="testoNormale" property="legame.livAut"
							disabled="true">
							<html:optionsCollection property="listaStatoControllo"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="etichetta" scope="col">
						<bean:message key="ricerca.simbolo"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:text property="legame.simbolo" readonly="true"></html:text>
					</td>
				</tr>
			</table>
			<table width="100%" border="0">
				<tr>
					<td align="center" class="etichetta">
						<bean:message key="ricerca.descrizione"
							bundle="gestioneSemanticaLabels" />
					</td>
				</tr>
				<tr>
					<td>
						<html:text styleId="testoNormale" property="legame.descrizione"
							size="138" readonly="true"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="center">
						<bean:message key="crea.ulteriore"
							bundle="gestioneSemanticaLabels" />
					</td>
				</tr>
				<tr>
					<td>
						<html:text styleId="testoNormale" property="legame.ulterioreTermine"
							size="138" readonly="true"></html:text>
					</td>
				</tr>
			</table>
			</sbn:disableAll>
			<table width="100%" border="0">
				<tr>
					<td align="left" class="etichetta" scope="col">
						<bean:message key="esamina.inserito"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td class="etichetta">
						<bean:message key="esamina.il" bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:text styleId="testoNormale" property="legame.dataIns"
							size="14" maxlength="20" readonly="true"></html:text>
					</td>
					<td class="etichetta">
						<bean:message key="esamina.modificato"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td class="etichetta">
						<bean:message key="esamina.il" bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:text styleId="testoNormale" property="legame.dataAgg"
							size="14" maxlength="20" readonly="true"></html:text>
					</td>
				</tr>

			</table>

			<div id="divFooter">
				<table align="center">
					<tr>
						<td align="center">
							<sbn:bottoniera buttons="pulsanti" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</sbn:navform>
</layout:page>

