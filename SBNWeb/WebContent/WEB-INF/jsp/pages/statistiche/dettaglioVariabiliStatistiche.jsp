<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<div id="divForm">
		<sbn:navform action="/statistiche/dettaglioVariabiliStatistiche.do">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<br>
			<table width="100%">
				<tr>
					<td colspan="2">
						<div align="center" class="testoNormale">
							Titolo query:&nbsp;&nbsp;&nbsp;
							<bean-struts:write name="dettaglioVariabiliStatisticheForm"
								property="statistica.nomeStatistica" />
						</div></td>
				</tr>
			</table>
			<br>
			<c:choose>
				<c:when
					test="${dettaglioVariabiliStatisticheForm.statistica.tipoQuery ne '0'}">
					<table width="100%">
						<logic:iterate id="item" property="statistica.elencoVariabili"
							name="dettaglioVariabiliStatisticheForm" indexId="riga">
							<tr class="testoNormale">
								<td width="18%" align="right">
									<div class="testoNormale">
										<bean-struts:write name="item" property="etichettaNomeVar" />
									</div></td>
								<td><c:choose>
										<c:when test="${item.obbligatorio eq 'S'}">
							*</c:when>
										<c:otherwise></c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${item.tipoVar eq 'combo'}">
											<html:select style="width:180px"
												property='<%= "statistica.elencoVariabili[" + riga + "].valore" %>'>
												<html:optionsCollection
													property='<%= "statistica.elencoVariabili[" + riga + "].listaCodiciVO" %>'
													value="codice" label="descrizione" />
											</html:select>
										</c:when>
										<c:when test="${item.tipoVar eq 'comboLibera'}">
											<html:select style="width:180px"
												property='<%= "statistica.elencoVariabili[" + riga + "].valore" %>'>
												<html:optionsCollection
													property='<%= "statistica.elencoVariabili[" + riga + "].listaCodiciVO" %>'
													value="codice" label="descrizione" />
											</html:select>
										</c:when>
										<c:when test="${item.tipoVar eq 'filtroListaBib' or item.tipoVar eq 'filtroListaBibNoSplit'}">
											<c:choose>
												<c:when
													test="${dettaglioVariabiliStatisticheForm.statistica.flPoloBiblio eq 'P'}">
													<html:text disabled="true" style="width:100px" size="10"
														name="dettaglioVariabiliStatisticheForm"
														property='<%= "statistica.elencoVariabili[" + riga + "].valore" %>'></html:text>
											&nbsp; <html:submit styleClass="buttonImageListaSezione"
														property="methodDettVarStatistiche">
														<bean:message key="button.cercabiblioteche"
															bundle="esportaLabels" />
													</html:submit>
													<c:choose>
														<c:when
															test="${dettaglioVariabiliStatisticheForm.tastoCancBib}">
															<html:submit property="methodDettVarStatistiche">
																<bean:message key="button.tutteLeBiblio"
																	bundle="esportaLabels" />
															</html:submit>
														</c:when>
													</c:choose>
												</c:when>
												<c:otherwise>
													<html:text disabled="true" style="width:100px" size="10"
														name="dettaglioVariabiliStatisticheForm"
														property='<%= "statistica.elencoVariabili[" + riga + "].valore" %>'></html:text>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<html:text
												disabled="${dettaglioVariabiliStatisticheForm.disable}"
												style="width:100px" size="10"
												name="dettaglioVariabiliStatisticheForm"
												property='<%= "statistica.elencoVariabili[" + riga + "].valore" %>'></html:text>
										</c:otherwise>
									</c:choose> <c:choose>
										<c:when test="${item.tipoVar eq 'data'}">&nbsp;&nbsp;(gg/mm/aaaa) </c:when>
									</c:choose>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</c:when>
			</c:choose>
			<BR>
			<c:choose>

				<c:when
					test="${dettaglioVariabiliStatisticheForm.obbligatorio eq 'S'}">
					<table>
						<tr>
							<td><div style="font-style: oblique;">&nbsp;&nbsp;I
									campi contrassegnati da&nbsp;&nbsp;*&nbsp;&nbsp;sono
									obbligatori</div>
							</td>
						</tr>
					</table>
				</c:when>
			</c:choose>
			<%--


		--%>
			<div id="divFooter">
				<table border="0" style="height: 40px" align="center">
					<tr>
						<c:choose>
							<c:when
								test="${dettaglioVariabiliStatisticheForm.disable == false}">
								<td width="100%"><html:submit styleClass="pulsanti"
										property="methodDettVarStatistiche">
										<bean:message key="button.conferma" bundle="statisticheLabels" />
									</html:submit> <html:submit styleClass="pulsanti"
										property="methodDettVarStatistiche">
										<bean:message key="button.indietro" bundle="statisticheLabels" />
									</html:submit>
								</td>
							</c:when>
							<c:otherwise>
								<td width="100%"><html:submit styleClass="pulsanti"
										property="methodDettVarStatistiche">
										<bean:message key="button.indietro" bundle="statisticheLabels" />
									</html:submit>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</table>
			</div>
		</sbn:navform>
	</div>
</layout:page>