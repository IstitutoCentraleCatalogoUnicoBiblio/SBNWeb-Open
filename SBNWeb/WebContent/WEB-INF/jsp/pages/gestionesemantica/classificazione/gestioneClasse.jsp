
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionesemantica/classificazione/GestioneClasse.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<br />
		<sbn:disableAll disabled="${navForm.enableConferma}">
		<table border="0">
			<c:choose>
				<c:when test="${navForm.dettClaGen.livelloPolo}">
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
					property="ricercaClasse.codSistemaClassificazione" disabled="true" styleId="cod_sistema" onchange="selectCodSistema()">
					<html:optionsCollection property="listaSistemiClassificazione"
						value="codice" label="codice" />
				</html:select></td>
				<td class="etichetta"><bean:message key="ricerca.edizione"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="ricercaClasse.codEdizioneDewey" disabled="true" styleId="edizione">
					<html:optionsCollection property="listaEdizioni" value="cd_unimarcTrim"
						label="ds_tabella" />
				</html:select></td>
				<td class="etichetta"><bean:message key="crea.statoDiControllo"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="statoControllo">
					<html:optionsCollection property="listaStatoControllo"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td class="etichetta" scope="col"><bean:message
					key="ricerca.simbolo" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text property="dettClaGen.simboloDewey.simbolo" readonly="true">
				</html:text></td>
				<td><html:checkbox name="navForm" property="dettClaGen.costruito">
					<bean:message key="gestionesemantica.classe.costruito" bundle="gestioneSemanticaLabels" />
				</html:checkbox>
				<html:hidden name="navForm" property="dettClaGen.costruito" value="false" />
				</td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="ricerca.descrizione" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="descrizione" cols="90" rows="6"></html:textarea>
					<sbn:tastiera limit="240" name="navForm" property="descrizione"/>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="crea.ulteriore" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="ulterioreTermine" cols="90" rows="6"></html:textarea>
				<sbn:tastiera limit="240" name="navForm" property="ulterioreTermine"/>
				</td>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr>
				<td align="left" class="etichetta" scope="col"><bean:message
					key="esamina.inserito" bundle="gestioneSemanticaLabels" /></td>

				<td class="etichetta"><bean:message key="esamina.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="dataInserimento" size="14" maxlength="20" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="esamina.modificato"
					bundle="gestioneSemanticaLabels" /></td>

				<td class="etichetta"><bean:message key="esamina.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale" property="dataModifica"
					size="14" maxlength="20" readonly="true"></html:text></td>
			</tr>
		</table>

		<!-- Titoli collegati -->
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta" scope="col"><bean:message
					key="classificazione.titoli" bundle="gestioneSemanticaLabels" /></td>
				<td align="center" class="etichetta" scope="col"><bean:message
					key="gestione.polo" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale" property="dettClaGen.numTitoliPolo"
					size="10" readonly="true"></html:text></td>
				<td align="center" class="etichetta" scope="col"><bean:message
					key="gestione.biblio" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="dettClaGen.numTitoliBiblio" size="10" readonly="true"></html:text></td>
			</tr>
		</table>
		</sbn:disableAll>
		</div>
		<div id="divFooter">
		<table align="center">
			<c:choose>
				<c:when test="${navForm.enableConferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/classificazione/bottonieraGestione.jsp" />
				</c:otherwise>
			</c:choose>
		</table>
		</div>
	</sbn:navform>
</layout:page>
<script type="text/javascript" src='<c:url value="/scripts/semantica/classi/ricercaClasse.js" />'></script>