
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
	<sbn:navform action="/gestionesemantica/classificazione/ImportaClasse.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
		<br />
		<table border="0">
			<c:choose>
				<c:when test="${ImportaClasseForm.dettClaGen.livelloPolo}">
					<table>
						<tr>
							<td class="etichetta"><bean:message
								key="gestionesemantica.classi.ricercaBasePolo"
								bundle="gestioneSemanticaLabels" />
							</td>
						</tr>
					</table>
					<br/>
				</c:when>
				<c:otherwise>
					<table>
						<tr>
							<td class="etichetta"><bean:message
								key="gestionesemantica.classi.ricercaBaseIndice"
								bundle="gestioneSemanticaLabels" />
							</td>
						</tr>
					</table>
					<br/>
				</c:otherwise>
			</c:choose>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.sistema"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="ricercaClasse.codSistemaClassificazione" disabled="true">
					<html:optionsCollection property="listaSistemiClassificazione"
						value="codice" label="codice" />
				</html:select></td>
				<td class="etichetta"><bean:message key="ricerca.edizione"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="ricercaClasse.codEdizioneDewey" disabled="true">
					<html:optionsCollection property="listaEdizioni" value="codice"
						label="descrizione" />
				</html:select></td>
				<td class="etichetta"><bean:message key="crea.statoDiControllo"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="codStatoControllo">
					<html:optionsCollection property="listaStatoControllo"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td class="etichetta" scope="col"><bean:message
					key="ricerca.simbolo" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text property="dettClaGen.simboloDewey.simbolo" readonly="true">
				</html:text></td>
				<td><html:checkbox name="ImportaClasseForm" property="dettClaGen.costruito">
					<bean:message key="gestionesemantica.classe.costruito" bundle="gestioneSemanticaLabels" />
				</html:checkbox>
				<html:hidden name="ImportaClasseForm" property="dettClaGen.costruito" value="false" />
				</td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="ricerca.descrizione" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale" property="descrizione"
					cols="90" rows="6"></html:textarea></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="crea.ulteriore" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="ulterioreTermine" cols="90" rows="6"></html:textarea></td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="left" class="etichetta" scope="col"><bean:message
					key="crea.inserito" bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="crea.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale" property="dataInserimento"
					size="14" maxlength="20" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="crea.modificato"
					bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="crea.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale" property="dataModifica"
					size="14" maxlength="20" readonly="true"></html:text></td>
			</tr>
		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodImpCla">
					<bean:message key="button.importa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodImpCla">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

