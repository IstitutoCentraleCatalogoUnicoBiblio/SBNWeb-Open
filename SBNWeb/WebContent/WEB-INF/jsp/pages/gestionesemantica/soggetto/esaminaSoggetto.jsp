<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/EsaminaSoggetto.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!-- Intestazione --> <c:choose>
			<c:when test="${navForm.enableEsamina}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/intestazioneEsaminaSogPolo.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/intestazioneEsaminaSogIndice.jsp" />
			</c:otherwise>
		</c:choose>

		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="esamina.testo" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="dettSogGenVO.testo" cols="90" rows="6" readonly="true"></html:textarea></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="esamina.note" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="dettSogGenVO.note" cols="90" rows="6" readonly="true"></html:textarea></td>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr>
				<td align="left" class="etichetta" scope="col"><bean:message
					key="esamina.inserito" bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="esamina.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="dettSogGenVO.dataIns" size="14" maxlength="20"
					readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="esamina.modificato"
					bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="esamina.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="dettSogGenVO.dataAgg" size="14" maxlength="20"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<!-- Titoli collegati -->
		<table width="100%" border="0">
			<c:choose>
				<c:when test="${navForm.enableEsamina}">
					<tr>
						<td align="center" class="etichetta" scope="col"><bean:message
							key="esamina.titoli" bundle="gestioneSemanticaLabels" /></td>

						<td align="center" class="etichetta" scope="col"><bean:message
							key="esamina.polo" bundle="gestioneSemanticaLabels" />
						<td><html:text styleId="testoNormale"
							property="dettSogGenVO.numTitoliPolo" size="10" readonly="true"></html:text></td>

						<td align="center" class="etichetta" scope="col"><bean:message
							key="esamina.biblio" bundle="gestioneSemanticaLabels" />
						<td><html:text styleId="testoNormale"
							property="dettSogGenVO.numTitoliBiblio" size="10" readonly="true"></html:text></td>
					</tr>
				</c:when>
				<c:otherwise>
					<!--<tr>
						<td align="center" class="etichetta" scope="col"><bean:message
							key="esamina.titoli" bundle="gestioneSemanticaLabels" /></td>
						<c:choose>
							<c:when test="${navForm.enableNumIndice}">
								<td align="center"><html:submit property="methodEsaSog">
									<bean:message key="button.indice"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<td align="center" class="etichetta" scope="col"><bean:message
									key="esamina.indice" bundle="gestioneSemanticaLabels" />
							</c:otherwise>
						</c:choose>
						<td><html:text styleId="testoNormale"
							property="numTitoliIndice" size="10" readonly="true"></html:text></td>
					</tr>
				-->
				</c:otherwise>
			</c:choose>
		</table>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<sbn:checkAttivita idControllo="MODIFICA">
					<td align="center"><logic:equal name="navForm"
						property="enableGestione" value="true">
						<html:submit property="methodEsaSog">
							<bean:message key="button.gestione"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</logic:equal></td>
				</sbn:checkAttivita>
				<sbn:checkAttivita idControllo="CREA">
					<td align="center"><logic:equal name="navForm"
						property="enableImporta" value="true">
						<html:submit property="methodEsaSog">
							<bean:message key="button.importa"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</logic:equal></td>
				</sbn:checkAttivita>
				<td align="center"><html:submit property="methodEsaSog">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodEsaSog">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>

				<td>
				<layout:combo bundle="gestioneSemanticaLabels"
					label="button.esamina" name="navForm"
					button="button.esegui" property="idFunzioneEsamina"
					combo="comboGestioneEsamina" parameter="methodEsaSog" /></td>

			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
