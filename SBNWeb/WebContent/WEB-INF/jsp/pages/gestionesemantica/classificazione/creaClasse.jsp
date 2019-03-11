<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/classificazione/CreaClasse.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
		<c:choose>
			<c:when test="${CreaClasseForm.enableTit}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
			</c:when>
			<c:otherwise>

			</c:otherwise>
		</c:choose>
		<table width="100%" border="0">
			<tr>
				<td class="etichetta">Base dati in aggiornamento Locale</td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td class="etichetta"><bean:message key="ricerca.sistema"
					bundle="gestioneSemanticaLabels" /></td>

				<td><html:select styleClass="testoNormale"
					property="ricercaClasse.codSistemaClassificazione" styleId="cod_sistema" onchange="selectCodSistema()">
					<html:optionsCollection property="listaSistemiClassificazione"
						value="codice" label="codice" />
				</html:select></td>
				<td class="etichetta"><bean:message key="ricerca.edizione"
					bundle="gestioneSemanticaLabels" /></td>
				<c:choose>
					<c:when test="${!CreaClasseForm.enableEdizione}">
						<td><html:select styleClass="testoNormale"
							property="ricercaClasse.codEdizioneDewey" styleId="edizione">
							<html:optionsCollection property="listaEdizioni" value="cd_unimarcTrim"
								label="ds_tabella" />
						</html:select></td>
					</c:when>
					<c:otherwise>
						<td><html:select styleClass="testoNormale"
							property="ricercaClasse.codEdizioneDewey" disabled="true">
							<html:optionsCollection property="listaEdizioni" value="codice"
								label="descrizione" />
						</html:select></td>
					</c:otherwise>
				</c:choose>
				<td class="etichetta"><bean:message key="crea.statoDiControllo"
					bundle="gestioneSemanticaLabels" /></td>
				<c:choose>
					<c:when test="${!CreaClasseForm.enableStato}">
						<td><html:select styleClass="testoNormale"
							property="codStatoControllo">
							<html:optionsCollection property="listaStatoControllo"
								value="codice" label="descrizione" />
						</html:select></td>
					</c:when>
					<c:otherwise>
						<td><html:select styleClass="testoNormale"
							property="codStatoControllo" disabled="true">
							<html:optionsCollection property="listaStatoControllo"
								value="codice" label="descrizione" />
						</html:select></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td class="etichetta" scope="col" align="center"><bean:message
					key="ricerca.simbolo" bundle="gestioneSemanticaLabels" /></td>
				<c:choose>
					<c:when test="${!CreaClasseForm.enableSimbolo}">
						<td><html:text styleId="testoNormale"
							property="ricercaClasse.simbolo"
							disabled="true">
						</html:text></td>
					</c:when>
					<c:otherwise>
						<td><html:text styleId="testoNormale"
							property="ricercaClasse.simbolo" >
						</html:text></td>
					</c:otherwise>
				</c:choose>
				<td>
					<html:checkbox name="CreaClasseForm" property="costruito" value="true" ><bean:message
					key="gestionesemantica.classe.costruito" bundle="gestioneSemanticaLabels" /></html:checkbox>
					<html:hidden name="CreaClasseForm" property="costruito" />
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
					cols="90" rows="6"></html:textarea>
					<sbn:tastiera limit="240" name="CreaClasseForm" property="descrizione"/></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="crea.ulteriore" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="ulterioreTermine" cols="90" rows="6"></html:textarea>
					<sbn:tastiera limit="240" name="CreaClasseForm" property="ulterioreTermine"/>
				</td>
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
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<td class="etichetta"><bean:message key="classificazione.titoli"
					bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="crea.polo"
					bundle="gestioneSemanticaLabels" />
				<td><html:text styleId="testoNormale" property="numNotiziePolo"
					size="15" maxlength="10" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="crea.biblio"
					bundle="gestioneSemanticaLabels" />
				<td><html:text styleId="testoNormale" property="numNotizieBiblio"
					size="15" maxlength="10" readonly="true"></html:text></td>
			</tr>

		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodCreaCla">
					<bean:message key="button.salvaSog"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodCreaCla">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodCreaCla">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
<script type="text/javascript" src='<c:url value="/scripts/semantica/classi/ricercaClasse.js" />'></script>