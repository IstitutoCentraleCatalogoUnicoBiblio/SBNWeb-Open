<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<%@page import="it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario.ProfilazioneSemanticaForm;"%>
<html:xhtml />
<layout:page>
<div id="divForm">

<sbn:navform action="/amministrazionesistema/abilitazioneBibliotecario/profilazioneBibliotecarioSemantica.do">
	<div id="divMessaggio">
			<sbn:errors bundle="amministrazioneSistemaMessages" />
	</div>

		<table align="center" border="0" width="100%">
			<tr>
				<td align="left" style="font-weight: bold; font-size: 15px" width="100%">
					<bean:message key="profilo.polo.parametri.sem.titolo" bundle="amministrazioneSistemaLabels"/>:
						<c:choose>
							<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'CLA'}">
								<bean:message key="profilo.polo.parametri.classificazioni"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'SOGG'}">
								<bean:message key="profilo.polo.parametri.soggetti"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'THE'}">
								<bean:message key="profilo.polo.parametri.thesauri"	bundle="amministrazioneSistemaLabels" />
							</c:when>
						</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<b><bean:message key="profilo.bibliotecario.parametri.nome" bundle="amministrazioneSistemaLabels"/>: </b>
					<c:out value="${profilazioneBibliotecarioSemanticaForm.utente}"></c:out>
					<b><bean:message key="profilo.bibliotecario.parametri.biblioteca" bundle="amministrazioneSistemaLabels"/>: </b>
					<c:out value="${profilazioneBibliotecarioSemanticaForm.nomeBib}"></c:out>
					<b><bean:message key="profilo.bibliotecario.parametri.user" bundle="amministrazioneSistemaLabels"/>: </b>
					<c:out value="${profilazioneBibliotecarioSemanticaForm.nomeUs}"></c:out>
				</td>
			</tr>
		</table>

			<br/>

			 <table class="sintetica" style="width: 600px">
			 	<tr>
			 		<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
			 		</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.nome"
							bundle="amministrazioneSistemaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<c:choose>
							<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'CLA'}">
								<bean:message key="profilo.polo.parametri.classificazione"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'SOGG'}">
								<bean:message key="profilo.polo.parametri.soggetto"	bundle="amministrazioneSistemaLabels" />
							</c:when>
							<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'THE'}">
								<bean:message key="profilo.polo.parametri.thesauro"	bundle="amministrazioneSistemaLabels" />
							</c:when>
						</c:choose>

					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.sololocale"
							bundle="amministrazioneSistemaLabels" />
					</th>
			    </tr>
				<bean-struts:define id="color" value="#FEF1E2" />

				<logic:iterate id="item" property="elencoParSemantica" name="profilazioneBibliotecarioSemanticaForm" indexId="riga">
		 			<c:if test="${item.acceso eq 'TRUE'}">
				 		<sbn:rowcolor var="color" index="riga" />
							<tr bgcolor="${color}">
								<c:choose>
									<c:when test="${profilazioneBibliotecarioSemanticaForm.nuovo eq 'TRUE'}">
										<td  style="text-align: center">
											<html:checkbox property="elencoParSemantica[${item.indice}].selezioneCheck" disabled="true"></html:checkbox>
										</td>
									</c:when>
									<c:otherwise>
										<td  style="text-align: center">
											<html:checkbox property="elencoParSemantica[${item.indice}].selezioneCheck"></html:checkbox>
										</td>
									</c:otherwise>
								</c:choose>
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
												<c:choose>
													<c:when test="${profilazioneBibliotecarioSemanticaForm.nuovo eq 'TRUE'}">
														<td  style="text-align: center">
															<logic:iterate id="opzione" property="radioOptions" name="pmtr">
																<html:radio property="elencoParSemantica[${item.indice}].elencoParametri[${pmtr.index}].selezione" value="${opzione}" disabled="true"></html:radio>
																<c:out value="${opzione}"></c:out>
															</logic:iterate>
														</td>
													</c:when>
													<c:otherwise>
														<td  style="text-align: center">
															<logic:iterate id="opzione" property="radioOptions" name="pmtr">
																<html:radio property="elencoParSemantica[${item.indice}].elencoParametri[${pmtr.index}].selezione" value="${opzione}"></html:radio>
																<c:out value="${opzione}"></c:out>
															</logic:iterate>
														</td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${pmtr.tipo eq 'TESTO'}">
												<td  style="text-align: left">
														<b><c:out value="${pmtr.selezione}"></c:out></b>
												</td>
											</c:when>
										</c:choose>
								</logic:iterate>
							</tr>
					</c:if>
				</logic:iterate>
				<c:if test="${profilazioneBibliotecarioSemanticaForm.nuovo eq 'TRUE'}">
						<tr bgcolor="${color}">
							<td  style="text-align: center">
								<html:checkbox property="checkTemp" disabled="true"></html:checkbox>
							</td>
							<td  style="text-align: left">
								<c:choose>
									<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'CLA'}">
										<b><bean:message key="profilo.polo.parametri.nuovo.classificazioni"	bundle="amministrazioneSistemaLabels" /></b>
									</c:when>
									<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'SOGG'}">
										<b><bean:message key="profilo.polo.parametri.nuovo.soggetti"	bundle="amministrazioneSistemaLabels" /></b>
									</c:when>
									<c:when test="${profilazioneBibliotecarioSemanticaForm.tipologia eq 'THE'}">
										<b><bean:message key="profilo.polo.parametri.nuovo.thesauri"	bundle="amministrazioneSistemaLabels" /></b>
									</c:when>
								</c:choose>
							</td>
							<td  style="text-align: center">
								<html:select property='selezioneNuovo'>
									<html:optionsCollection property="elencoScelteNuovo" value="codice" label="descrizione"/>
								</html:select>
							</td>
							<td  style="text-align: center">
								<html:radio property="temp" value="temp" disabled="true"></html:radio>
								<c:out value="Si'"></c:out>
								<html:radio property="temp" value="temp" disabled="true"></html:radio>
								<c:out value="No"></c:out>
							</td>
						</tr>
				</c:if>

			</table>


	<hr/>

		<div id="divFooter">
			<table align="center" border="0" style="height:40px">
				<tr>
					<c:choose>
					<c:when test="${profilazioneBibliotecarioSemanticaForm.nuovo eq 'TRUE'}">
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
								<c:when test="${profilazioneBibliotecarioSemanticaForm.abilitatoWrite eq 'TRUE'}">
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