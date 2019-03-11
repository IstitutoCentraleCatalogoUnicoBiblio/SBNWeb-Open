<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table align="center">
	<tr>
		<c:choose>
			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_SOGGETTI'}">
				<c:choose>
					<c:when test="${!CatalogazioneSemanticaForm.enableIndice}">

						<sbn:checkAttivita idControllo="GESTIONE">
							<td class="etichetta"><bean:message
								key="catalogazione.soggetto" bundle="gestioneSemanticaLabels" />
							</td>
							<td align="center"><html:submit
								property="methodCatalogazione">
								<bean:message key="button.vaia" bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</sbn:checkAttivita>
						<td><layout:combo bundle="gestioneSemanticaLabels"
							label="catalogazione.legame" name="CatalogazioneSemanticaForm"
							button="button.conferma" property="idFunzioneLegame"
							combo="comboGestioneLegame" parameter="methodCatalogazione" /></td>

						<sbn:checkAttivita idControllo="RANKING">
							<td><html:submit property="methodCatalogazione"
									styleClass="buttonFrecciaSu"
									titleKey="servizi.bottone.frecciaSu" bundle="serviziLabels">
									<bean:message key="servizi.bottone.frecciaSu"
										bundle="serviziLabels" />
								</html:submit></td>
							<td><html:submit property="methodCatalogazione"
									styleClass="buttonFrecciaGiu"
									titleKey="servizi.bottone.frecciaGiu" bundle="serviziLabels">
									<bean:message key="servizi.bottone.frecciaGiu"
										bundle="serviziLabels" />
								</html:submit></td>
						</sbn:checkAttivita>

						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.cercaIndice"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>

						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.chiudi"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_CLASSI'}">
				<c:choose>
					<c:when test="${!CatalogazioneSemanticaForm.enableIndice}">
						<sbn:checkAttivita idControllo="GESTIONE">
							<td class="etichetta"><bean:message
								key="catalogazione.classificazione"
								bundle="gestioneSemanticaLabels" /></td>
							<td align="center"><html:submit
								property="methodCatalogazione">
								<bean:message key="button.vaia" bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</sbn:checkAttivita>
						<td><layout:combo bundle="gestioneSemanticaLabels"
							label="catalogazione.legame" name="CatalogazioneSemanticaForm"
							button="button.conferma" property="idFunzioneLegame"
							combo="comboGestioneLegame" parameter="methodCatalogazione" /></td>

						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.cercaIndice"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.chiudi"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</c:when>

			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_THESAURO'}">
				<sbn:checkAttivita idControllo="GESTIONE">
					<td class="etichetta"><bean:message
						key="gestionesemantica.thesauro.thesauro"
						bundle="gestioneSemanticaLabels" /></td>
					<td align="center"><html:submit property="methodCatalogazione">
						<bean:message key="button.vaia" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
				<td><layout:combo bundle="gestioneSemanticaLabels"
					label="catalogazione.legame" name="CatalogazioneSemanticaForm"
					button="button.conferma" property="idFunzioneLegame"
					combo="comboGestioneLegame" parameter="methodCatalogazione" /></td>

				<td align="center"><html:submit property="methodCatalogazione">
					<bean:message key="button.chiudi" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</c:when>

			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_ABSTRACT'}">
				<c:choose>
					<c:when test="${CatalogazioneSemanticaForm.enableOk}">
						<logic:equal name="CatalogazioneSemanticaForm" property="enableOk"
							value="true">
							<td align="center"><html:submit
								property="methodCatalogazione">
								<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<logic:equal name="CatalogazioneSemanticaForm"
							property="enableElimina" value="true">
							<td align="center"><html:submit
								property="methodCatalogazione">
								<bean:message key="button.elimina"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<logic:equal name="CatalogazioneSemanticaForm"
							property="enableModifica" value="true">
							<c:if test="${!CatalogazioneSemanticaForm.enableSoloEsamina}">
								<td align="center"><html:submit
									property="methodCatalogazione">
									<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
								</html:submit></td>
							</c:if>
						</logic:equal>
						<logic:equal name="CatalogazioneSemanticaForm"
							property="enableElimina" value="true">
							<td align="center"><html:submit
								property="methodCatalogazione">
								<bean:message key="button.elimina"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</c:when>

			<c:otherwise>
			</c:otherwise>
		</c:choose>

	</tr>
</table>
