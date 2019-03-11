<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<bean-struts:define id="area2"  value="false"/>
<bean-struts:define id="area3"  value="false"/>
<bean-struts:define id="area4"  value="false"/>
<bean-struts:define id="area5"  value="false"/>


<c:choose>
<c:when test="${rinnovoAutorizzazioniUtenteForm.tipoRinnModalitaDiff eq 'D'}">
	<bean-struts:define id="area2"  value="true"/>
	<bean-struts:define id="area3"  value="true"/>
</c:when>
</c:choose>

<c:choose>
<c:when test="${rinnovoAutorizzazioniUtenteForm.tipoRinnModalitaDiff eq 'T'}">
	<bean-struts:define id="area3"  value="true"/>
	<bean-struts:define id="area4"  value="true"/>
</c:when>
</c:choose>

<c:choose>
<c:when test="${rinnovoAutorizzazioniUtenteForm.tipoRinnModalitaDiff eq 'U'}">
	<bean-struts:define id="area2"  value="true"/>
	<bean-struts:define id="area4"  value="true"/>
	<bean-struts:define id="area5"  value="true"/>
</c:when>
</c:choose>

<c:choose>
<c:when test="${rinnovoAutorizzazioniUtenteForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
	<bean-struts:define id="area2"  value="true"/>
	<bean-struts:define id="area3"  value="true"/>
	<bean-struts:define id="area4"  value="true"/>

</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/elaborazioniDifferite/rinnovoAutorizzazioniUtente.do">
		<div id="divForm">
			<div id="divMessaggio"><sbn:errors /></div>
			<br>
			<table width="100%" border="0" >
				<tr>
					<td>
						<table width="100%" border="0" >
							<tr>
								<td width="20%" >
								<div class="etichetta">
									<bean:message	key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" />
									<html:text disabled="true" styleId="testoNormale" property="codBib"	size="5" maxlength="3"></html:text>
									<span disabled="true">
									<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodRinnAutUte" >
											<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
									</html:submit>
									</span>
								 </div>
								</td>
								<td align="left">
									<bean-struts:write	name="rinnovoAutorizzazioniUtenteForm" property="descrBib" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit1" bundle="serviziLabels" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit2" bundle="serviziLabels" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit3" bundle="serviziLabels" />
								</td>
							</tr>
							<!--
							<tr>
								<td colspan="2">
									<bean:message  key="servizi.archiviazioneMovimenti.tit" bundle="serviziLabels" />
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td align="right" class="etichetta">
									<bean:message key="servizi.archiviazioneMovimenti.data" bundle="serviziLabels" />
								</td>
								<td align="left">
									<html:text styleId="testoNoBold" property="dataSvecchiamento" size="10" ></html:text>
								</td>
							</tr>
							-->
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" >
					      <tr>
						    <td>
						     	<hr color="#dde8f0"/>
						    </td>
						  </tr>
						</table>
					</td>
				</tr>

				<tr>
					<td>
						<table width="100%" border="0" >
							<tr>
								<td>
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit4" bundle="serviziLabels" />
								</td>
							</tr>

							<tr>
								<td>
							        <html:radio property="tipoRinnModalitaDiff" value="D" onchange="this.form.submit();" disabled="${noinput}" />
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit5" bundle="serviziLabels" />
								</td>
							</tr>
							<tr>
								<td>
							        <html:radio property="tipoRinnModalitaDiff" value="T" onchange="this.form.submit();" disabled="${noinput}"/>
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit6" bundle="serviziLabels" />
									&nbsp;<html:text styleId="testoNoBold"   property="dataRinnovoOpz2" size="10" maxlength="10" readonly="${area2}"></html:text>
								</td>
							</tr>
							<tr>
								<td>
							        <html:radio property="tipoRinnModalitaDiff" value="U" onchange="this.form.submit();" disabled="${noinput}" />
									<bean:message  key="servizi.utenti.rinnovoAutDiffTit7" bundle="serviziLabels" />
									&nbsp;<html:text styleId="testoNoBold"   property="dataRinnovoOpz3" size="10" maxlength="10" readonly="${area3}"></html:text>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<c:choose>
					<c:when test="${!area5}">
						<tr>
							<td>
							  	<table  align="left" width="80%" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
							  	<tr class="etichetta" bgcolor="#dde8f0">
									<td  scope="col" align="left" width="10%">
										<bean:message key="servizi.autorizzazioni.header.codAut" bundle="serviziLabels" />
									</td>
									<td  scope="col" align="left">
										<bean:message key="servizi.autorizzazioni.header.desSer" bundle="serviziLabels" />
									</td>
									<td  scope="col" align="left"  width="10%">
										<bean:message key="servizi.utenti.rinnovoAutDiffTit8" bundle="serviziLabels" />
									</td>
									<td scope="col" align="left"  width="5%"></td>
								</tr>
								<c:choose>
									<c:when test="${rinnovoAutorizzazioniUtenteForm.risultatiPresenti}">
										<logic:iterate id="elencaAut" property="listaAutorizzazioni"	name="rinnovoAutorizzazioniUtenteForm" indexId="indEle">
										   <c:set var="color" >
												<c:choose>
											        <c:when test='${color == "#FFCC99"}'>
											            #FEF1E2
											        </c:when>
											        <c:otherwise>
														#FFCC99
											        </c:otherwise>
											    </c:choose>
										    </c:set>
											<tr class="testoNormale" bgcolor="${color}">
												<td align="left"><bean-struts:write  name="elencaAut" property="codice1"/></td>
												<td align="left"><bean-struts:write  name="elencaAut" property="codice2"/></td>
												<td align="left">
													<html:text styleId="testoNoBold"  name="rinnovoAutorizzazioniUtenteForm" property='<%= "listaAutorizzazioni[" + indEle + "].codice3" %>' size="10" maxlength="10" readonly="${area4}" ></html:text>
												</td>
												<td>
													<html:multibox property="selectedAut" value="${elencaAut.codice1}" disabled="${noinput}" ></html:multibox>
												</td>
											</tr>
										</logic:iterate>
									</c:when>
								</c:choose>
								</table>

							</td>
						</tr>

					</c:when>
				</c:choose>

			</table>
		</div>
		<br><br>
		<div id="divFooter">
		<c:choose>
			<c:when test="${rinnovoAutorizzazioniUtenteForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
			<table align="center">
				<tr>
					<td align="center">

						<html:submit property="methodRinnAutUte" styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels">
							<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
						</html:submit>
						<html:submit property="methodRinnAutUte" styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels">
							<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodRinnAutUte">
							<bean:message key="servizi.bottone.conferma" bundle="serviziLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti"	property="methodRinnAutUte">
							<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
						</html:submit>

					</td>
				</tr>
			</table>

    		</c:otherwise>
		</c:choose>
		</div>
	</sbn:navform>
</layout:page>



