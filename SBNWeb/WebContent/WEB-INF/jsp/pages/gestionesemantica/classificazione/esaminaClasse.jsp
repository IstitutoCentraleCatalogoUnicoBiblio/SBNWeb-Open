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
		action="/gestionesemantica/classificazione/EsaminaClasse.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<br/>
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
		<c:if test="${EsaminaClasseForm.enableTit}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
		</c:if>
		<table border="0">
			<c:choose>
				<c:when test="${!EsaminaClasseForm.enableIndice}">
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
								<br/>
							</td>
						</tr>
					</table>
					<br/>
				</c:otherwise>
			</c:choose>
			<tr>
				<td class="etichetta">
					<bean:message key="ricerca.sistema"	bundle="gestioneSemanticaLabels" />
				</td>
				<td>
					<html:select styleClass="testoNormale"
						property="ricercaClasse.codSistemaClassificazione" disabled="true">
							<html:optionsCollection property="listaSistemiClassificazione"
								value="codice" label="codice" />
					</html:select>
				</td>
				<td class="etichetta">
					<bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" />
				</td>
				<td>
					<html:select styleClass="testoNormale"
						property="ricercaClasse.codEdizioneDewey" disabled="true">
							<html:optionsCollection property="listaEdizioni" value="codice"
								label="descrizione" />
					</html:select>
				</td>
				<td class="etichetta">
					<bean:message key="crea.statoDiControllo" bundle="gestioneSemanticaLabels" />
				</td>
				<td>
					<html:select styleClass="testoNormale"
						property="statoControllo" disabled="true">
							<html:optionsCollection property="listaStatoControllo"
								value="codice" label="descrizione" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="etichetta" scope="col"><bean:message
					key="ricerca.simbolo" bundle="gestioneSemanticaLabels" />
				</td>
				<td>
					<html:text property="dettClaGen.simboloDewey.simbolo" readonly="true" />
				</td>
				<td>
					<html:checkbox name="EsaminaClasseForm"
						property="dettClaGen.costruito" value="true" disabled="true">
							<bean:message key="gestionesemantica.classe.costruito"
								bundle="gestioneSemanticaLabels" />
					</html:checkbox>
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
					property="descrizione" cols="90" rows="6" readonly="true"></html:textarea>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="crea.ulteriore" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="ulterioreTermine" cols="90" rows="6" readonly="true"></html:textarea>
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
					property="dataInserimento" size="14" maxlength="20" readonly="true"></html:text>
				</td>
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
			<c:if test="${EsaminaClasseForm.enableEsamina}">
				<tr>
					<td align="center" class="etichetta" scope="col"><bean:message
						key="classificazione.titoli" bundle="gestioneSemanticaLabels" />
					</td>
					<td align="center" class="etichetta" scope="col"><bean:message
						key="esamina.polo" bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
						property="dettClaGen.numTitoliPolo" size="10" readonly="true"></html:text></td>
					<td align="center" class="etichetta" scope="col"><bean:message
						key="esamina.biblio" bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
						property="dettClaGen.numTitoliBiblio" size="10" readonly="true"></html:text>
					</td>
				</tr>
			</c:if>
		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${EsaminaClasseForm.enableGestione}">
						<sbn:checkAttivita idControllo="TRASCINA">
							<td align="center"><logic:equal name="EsaminaClasseForm"
								property="enableOk" value="true">
								<html:submit property="methodEsaCla">
									<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
								</html:submit>
							</logic:equal></td>
						</sbn:checkAttivita>
						<sbn:checkAttivita idControllo="MODIFICA">
							<td align="center"><logic:equal name="EsaminaClasseForm"
								property="enableGestione" value="true">
								<html:submit property="methodEsaCla">
									<bean:message key="button.gestione"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</logic:equal></td>
						</sbn:checkAttivita>
					</c:when>
					<c:otherwise>
						<sbn:checkAttivita idControllo="CREA">
							<td align="center"><logic:equal name="EsaminaClasseForm"
								property="enableImporta" value="true">
								<html:submit property="methodEsaCla">
									<bean:message key="button.importa"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</logic:equal></td>
						</sbn:checkAttivita>
						<c:if
							test="${EsaminaClasseForm.enableIndice and !EsaminaClasseForm.enableNumIndice}">
							<td align="center"><html:submit property="methodEsaCla">
								<bean:message key="button.elimina"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</c:if>
					</c:otherwise>
				</c:choose>
				<td align="center"><html:submit property="methodEsaCla">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodEsaCla">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td><layout:combo bundle="gestioneSemanticaLabels"
					label="button.esamina" name="EsaminaClasseForm"
					button="button.esegui" property="idFunzioneEsamina"
					combo="comboGestioneEsamina" parameter="methodEsaCla" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
