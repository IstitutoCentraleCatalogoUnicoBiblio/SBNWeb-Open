<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<%@page import="it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.ProfilazioneSemanticaForm;"%>
<html:xhtml />
<layout:page>
<div id="divForm">
<sbn:navform action="/amministrazionesistema/abilitazioneBiblioteca/profilazioneBibliotecaSemantica.do">
	<div id="divMessaggio">
			<sbn:errors bundle="amministrazioneSistemaMessages" />
	</div>

		<table align="center" border="0" width="100%">
			<tr>
				<td align="left" style="font-weight: bold; font-size: 15px" width="100%">
					<bean:message key="profilo.biblioteca.parametri.sem" bundle="amministrazioneSistemaLabels"/> "
					<c:out value="${profilazioneBibliotecaSemanticaForm.nomeBib}"></c:out>"
					<bean:message key="profilo.biblioteca.parametri.sem.tipo" bundle="amministrazioneSistemaLabels"/>:
						<c:choose>
							<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'CLA'}">
								<bean:message key="profilo.polo.parametri.classificazioni"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'SOGG'}">
								<bean:message key="profilo.polo.parametri.soggetti"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'THE'}">
								<bean:message key="profilo.polo.parametri.thesauri"	bundle="amministrazioneSistemaLabels" />
							</c:when>
						</c:choose>
				</td>
			</tr>
		</table>

			<br/><p/>

			 <table class="sintetica" style="width: 80%">
			 	<tr>
			 		<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
			 		</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.nome"
							bundle="amministrazioneSistemaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<c:choose>
							<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'CLA'}">
								<bean:message key="profilo.polo.parametri.classificazione"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'SOGG'}">
								<bean:message key="profilo.polo.parametri.soggetto"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'THE'}">
								<bean:message key="profilo.polo.parametri.thesauro"	bundle="amministrazioneSistemaLabels" />
							</c:when>
						</c:choose>

					</th>
					<c:choose>
						<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'CLA'}">
							<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
								<bean:message key="profilo.biblioteca.semantica.edizione" bundle="amministrazioneSistemaLabels" />
							</th>
						</c:when>
					</c:choose>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center; width: 20%">
						<bean:message key="profilo.biblioteca.semantica.utilizzato" bundle="amministrazioneSistemaLabels" />
					</th>
					<%--
					<c:choose>
						<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia ne 'CLA'}">
							<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
								<bean:message key="profilo.biblioteca.semantica.recupero" bundle="amministrazioneSistemaLabels" />
							</th>
						</c:when>
					</c:choose>
					 --%>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.sololocale"
							bundle="amministrazioneSistemaLabels" />
					</th>
			    </tr>
				<bean-struts:define id="color" value="#FEF1E2" />

				<logic:iterate id="item" property="elencoParSemantica" name="profilazioneBibliotecaSemanticaForm" indexId="riga">
		 			<c:if test="${item.acceso eq 'TRUE'}">
				 		<sbn:rowcolor var="color" index="riga" />
							<tr bgcolor="${color}">
								<td  style="text-align: center">
									<html:checkbox property="elencoParSemantica[${item.indice}].selezioneCheck"></html:checkbox>
								</td>
								<td  style="text-align: left">
									<b><c:out value="${item.codice}"></c:out> - <bean:message key="${item.descrizione}" bundle="amministrazioneSistemaLabels"/></b>
								</td>
								<logic:iterate id="pmtr" property="elencoParametri" name="item">
										<c:choose>
											<c:when test="${pmtr.tipo eq 'MENU'}">
												<td  style="text-align: center">
													<html:select property='elencoParSemantica[${item.indice}].elencoParametri[${pmtr.index}].selezione'>
														<html:optionsCollection property="elencoParSemantica[${item.indice}].elencoParametri[${pmtr.index}].elencoScelte" value="codice" label="descrizione"/>
													</html:select>
												</td>
											</c:when>
											<c:when test="${pmtr.tipo eq 'RADIO'}">
												<td  style="text-align: center">
													<logic:iterate id="opzione" property="radioOptions" name="pmtr">
														<html:radio property="elencoParSemantica[${item.indice}].elencoParametri[${pmtr.index}].selezione" value="${opzione}"></html:radio>
														<c:out value="${opzione}"></c:out>
													</logic:iterate>
												</td>
											</c:when>
											<c:when test="${pmtr.tipo eq 'CHECK'}">
												<td  style="text-align: center">
													<logic:iterate id="opzione" property="elencoCheck" name="pmtr">
														<c:out value="${opzione.descrizione}"></c:out>
														<html:checkbox property="elencoParSemantica[${item.indice}].elencoParametri[${pmtr.index}].elencoCheck[${opzione.indice}].selezione"></html:checkbox>
													</logic:iterate>
													</td>
											</c:when>
											<c:when test="${pmtr.tipo eq 'TESTO'}">
												<td  style="text-align: left">
														<b><c:out value="${pmtr.selezione}"></c:out></b>
												</td>
											</c:when>
											<c:when test="${pmtr.tipo eq 'MESSAGGIO'}">
												<td  style="text-align: center">
														<bean:message key="${pmtr.descrizione}" bundle="amministrazioneSistemaLabels"/>
												</td>
											</c:when>
										</c:choose>
								</logic:iterate>
							</tr>
					</c:if>
				</logic:iterate>
				<c:if test="${profilazioneBibliotecaSemanticaForm.nuovo eq 'TRUE'}">
						<tr bgcolor="${color}">
							<td  style="text-align: center">
								<html:checkbox property="checkTemp" disabled="true"></html:checkbox>
							</td>
							<td  style="text-align: left">
								<c:choose>
									<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'CLA'}">
										<b><bean:message key="profilo.polo.parametri.nuovo.classificazioni"	bundle="amministrazioneSistemaLabels" /></b>
									</c:when>
									<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'SOGG'}">
										<b><bean:message key="profilo.polo.parametri.nuovo.soggetti"	bundle="amministrazioneSistemaLabels" /></b>
									</c:when>
									<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'THE'}">
										<b><bean:message key="profilo.polo.parametri.nuovo.thesauri"	bundle="amministrazioneSistemaLabels" /></b>
									</c:when>
								</c:choose>
							</td>
							<td  style="text-align: center">
								<html:select property='selezioneNuovo'>
									<html:optionsCollection property="elencoScelteNuovo" value="codice" label="descrizione"/>
								</html:select>
							</td>
							<c:choose>
								<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia eq 'CLA'}">
									<td  style="text-align: center">
									</td>
								</c:when>
							</c:choose>
							<td  style="text-align: center">
								<html:radio property="temp" value="temp" disabled="true"></html:radio>
								<c:out value="Si'"></c:out>
								<html:radio property="temp" value="temp" disabled="true"></html:radio>
								<c:out value="No"></c:out>
							</td>
							<c:choose>
								<c:when test="${profilazioneBibliotecaSemanticaForm.tipologia ne 'CLA'}">
									<td  style="text-align: center">
										<html:radio property="temp" value="temp" disabled="true"></html:radio>
										<c:out value="Si'"></c:out>
										<html:radio property="temp" value="temp" disabled="true"></html:radio>
										<c:out value="No"></c:out>
									</td>
								</c:when>
							</c:choose>
							<%--
							<td  style="text-align: center">
								<html:radio property="temp" value="temp" disabled="true"></html:radio>
								<c:out value="Si'"></c:out>
								<html:radio property="temp" value="temp" disabled="true"></html:radio>
								<c:out value="No"></c:out>
							</td>
							 --%>
						</tr>
				</c:if>

			</table>


	<hr/>

		<div id="divFooter">
			<table align="center" border="0" style="height:40px">
				<tr>
					<c:choose>
					<c:when test="${profilazioneBibliotecaSemanticaForm.nuovo eq 'TRUE'}">
						<td align="center">
							<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem" disabled="true">
								<bean:message key="profilo.polo.torna" bundle="amministrazioneSistemaLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem">
								<bean:message key="profilo.polo.parametri.button.conferma.aggiungi" bundle="amministrazioneSistemaLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem" disabled="true">
								<bean:message key="profilo.polo.button.rimuovi" bundle="amministrazioneSistemaLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem" disabled="true">
								<bean:message key="profilo.polo.annulla" bundle="amministrazioneSistemaLabels" />
							</html:submit>
						</td>
					</c:when>
					<c:otherwise>
						<td align="center">
							<c:choose>
								<c:when test="${profilazioneBibliotecaSemanticaForm.abilitatoWrite eq 'TRUE'}">
									<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem">
										<bean:message key="profilo.polo.torna" bundle="amministrazioneSistemaLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem">
										<bean:message key="profilo.polo.button.aggiungi" bundle="amministrazioneSistemaLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem">
										<bean:message key="profilo.polo.button.rimuovi" bundle="amministrazioneSistemaLabels" />
									</html:submit>
								</c:when>
							</c:choose>
							<html:submit styleClass="pulsanti" property="methodProfilazioneParametriSem">
								<bean:message key="profilo.polo.annulla" bundle="amministrazioneSistemaLabels" />
							</html:submit>
						</td>
					</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>

    </sbn:navform>
  </layout:page>