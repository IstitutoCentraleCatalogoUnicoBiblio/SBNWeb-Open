<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/classificazione/RicercaClasse.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
			<l:notEmpty name="RicercaClasseForm"
				property="listaSistemiClassificazione">
				<l:notEmpty name="RicercaClasseForm" property="listaEdizioni">
					<l:notEmpty name="RicercaClasseForm"
						property="listaStatoControllo">
						<br />
						<table cellspacing="0" border="0" bgcolor="#FEF1E2">
							<tr>
								<td class="etichetta">
									<bean:message key="ricerca.sistema"
										bundle="gestioneSemanticaLabels" />
								</td>
								<td class="etichetta">
									<html:select styleClass="testoNormale" styleId="cod_sistema" onchange="selectCodSistema()"
										property="ricercaClasse.codSistemaClassificazione">
										<html:optionsCollection property="listaSistemiClassificazione" value="codice" label="codice" />
									</html:select>
									&nbsp;
									<bean:message key="ricerca.edizione"
										bundle="gestioneSemanticaLabels" />
									&nbsp;
									<html:select styleClass="testoNormale" property="ricercaClasse.codEdizioneDewey" styleId="edizione">
										<html:optionsCollection property="listaEdizioni"
											value="codice" label="descrizione" />
									</html:select>
									&nbsp;
									<bean:message key="ricerca.simbolo"
										bundle="gestioneSemanticaLabels" />
									&nbsp;
									<html:text styleId="testoNormale"
										property="ricercaClasse.simbolo"></html:text>
								</td>
								<td class="etichetta" align="right">
									&nbsp;
									<bean:message key="ricerca.puntuale"
										bundle="gestioneSemanticaLabels" />
									<html:checkbox property="ricercaClasse.puntuale" />
									<html:hidden property="ricercaClasse.puntuale" value="false"/>
								</td>
							<tr>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td class="etichetta">
									<bean:message key="gestionesemantica.soggetto.parole"
										bundle="gestioneSemanticaLabels" />
								</td>
								<td class="etichetta">
									<html:text styleId="testoNormale"
										property="ricercaClasse.parole" size="70" maxlength="70"></html:text>
									<sbn:tastiera limit="70" property="ricercaClasse.parole"
										name="RicercaClasseForm" />
								</td>
								<td class="etichetta" align="right">
									&nbsp;
									<!-- <bean:message key="ricerca.utilizzati"
										bundle="gestioneSemanticaLabels" />
									<html:checkbox property="ricercaClasse.utilizzati"></html:checkbox> -->
								</td>
							</tr>
							<tr>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
						<!-- <tr>
								<td class="etichetta">
									<bean:message key="ricerca.livello"
										bundle="gestioneSemanticaLabels" />
									&nbsp;
									<bean:message key="ricerca.da" bundle="gestioneSemanticaLabels" />
								</td>
								<td class="etichetta">
									<html:select styleClass="testoNormale"
										property="ricercaClasse.livelloAutoritaDa">
										<html:optionsCollection property="listaStatoControllo"
											value="codice" label="descrizione" />
									</html:select>
									&nbsp;
									<bean:message key="ricerca.a" bundle="gestioneSemanticaLabels" />
									&nbsp;
									<html:select styleClass="testoNormale"
										property="ricercaClasse.livelloAutoritaA">
										<html:optionsCollection property="listaStatoControllo"
											value="codice" label="descrizione" />
									</html:select>
								</td>

								<td>
									&nbsp;
								</td>
							</tr>  -->
							<tr>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td class="etichetta">
									<bean:message key="ricerca.ordinamento"
										bundle="gestioneSemanticaLabels" />
								</td>
								<td class="etichetta">
									<html:select styleClass="testoNormale"
										property="ricercaClasse.ordinamento">
										<html:optionsCollection property="listaOrdinamentoClasse"
											value="codice" label="descrizione" />
									</html:select>
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td class="etichetta">
									<bean:message key="ricerca.elementoPerBlocco"
										bundle="gestioneSemanticaLabels" />
								</td>
								<td class="etichetta">
									<html:text styleId="testoNormale"
										property="ricercaClasse.elemBlocco" size="5" maxlength="4" />
									&nbsp;
									<bean:message key="ricerca.livelloDiRicerca"
										bundle="gestioneSemanticaLabels" />
									:&nbsp;
									<bean:message key="ricerca.polo"
										bundle="gestioneSemanticaLabels" />
									&nbsp;
									<html:radio property="ricercaClasse.polo" value="true" />
									&nbsp;
									<bean:message key="ricerca.indice"
										bundle="gestioneSemanticaLabels" />
									&nbsp;
									<html:radio property="ricercaClasse.polo" value="false" />
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>

						<div id="divFooter">
							<table align="center">
								<tr>
									<td align="center">
										<html:submit property="methodRicercaClasse">
											<bean:message key="button.cerca"
												bundle="gestioneSemanticaLabels" />
										</html:submit>
										<l:equal name="RicercaClasseForm" property="enableCrea"
											value="true">
											<html:submit property="methodRicercaClasse">
												<bean:message key="button.crea"
													bundle="gestioneSemanticaLabels" />
											</html:submit>
										</l:equal>
										<c:if test="${!NAVIGATION_INSTANCE.first}">
											<html:submit property="methodRicercaClasse">
												<bean:message key="button.annulla"
													bundle="gestioneSemanticaLabels" />
											</html:submit>
										</c:if>
									</td>
								</tr>
							</table>
						</div>
					</l:notEmpty>
				</l:notEmpty>
			</l:notEmpty>
		</div>
	</sbn:navform>
</layout:page>
<script type="text/javascript" src='<c:url value="/scripts/semantica/classi/ricercaClasse.js" />'></script>