<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/autorizzazioni/DettaglioAutorizzazioni">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors bundle="serviziMessages" />
		</div>
		<br>
		<sbn:disableAll checkAttivita="GESTIONE">
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" /></td>
				<c:choose>
					<c:when test="${navForm.conferma}">
						<td><html:text styleId="testoNoBold"
							property="autAna.codBiblioteca" size="5"
							disabled="${navForm.conferma}"></html:text></td>
					</c:when>
					<c:otherwise>
						<td><html:text styleId="testoNoBold"
							property="autAna.codBiblioteca" size="5" disabled="true">
						</html:text></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="servizi.autorizzazioni.header.codAut" bundle="serviziLabels" />
				</td>
				<c:choose>
					<c:when test="${navForm.conferma}">
						<td><html:text styleId="testoNoBold"
							property="autAna.codAutorizzazione" size="5" maxlength="3"
							disabled="${navForm.conferma}"></html:text></td>
					</c:when>
					<c:otherwise>
						<td><html:text styleId="testoNoBold"
							property="autAna.codAutorizzazione" size="5" maxlength="3"
							disabled="${!navForm.autAna.nuovaAut}"></html:text>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="servizi.autorizzazioni.desAutorizzazione"
					bundle="serviziLabels" /></td>
				<td><html:text styleId="testoNoBold"
					property="autAna.desAutorizzazione" size="50" maxlength="160"
					disabled="${navForm.conferma}"></html:text></td>

			</tr>
			<tr>
				<td class="etichetta" align="right"><bean:message
					key="servizi.autorizzazioni.header.automaticaX"
					bundle="serviziLabels" /></td>
				<td align="left"><html:select property="autAna.automaticoPer"
					disabled="${navForm.conferma}">
					<html:optionsCollection property="elencoAutomaticoX" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>
		<br>
		<table class="sintetica">
			<tr><!--

				<th width="15" class="etichetta" align="center" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.bibliotecaUtente"
					bundle="serviziLabels" /></th>
				-->
				<th width="15" class="etichetta" align="center" bgcolor="#dde8f0">
<!--				<bean:message key="servizi.autorizzazioni.header.tipSer" bundle="serviziLabels" />-->
					<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
				</th>
				<th width="15" class="etichetta" align="center" bgcolor="#dde8f0">
<!--				<bean:message key="servizi.autorizzazioni.header.codSer" bundle="serviziLabels" /></th>-->
				<bean:message key="servizi.utenti.titServizio" bundle="serviziLabels" /></th>
				<!--
				<th width="15" class="etichetta" align="center" bgcolor="#dde8f0">
				<bean:message key="servizi.autorizzazioni.header.desSer"
					bundle="serviziLabels" /></th>
				-->
				<c:choose>
					<c:when test="${!navForm.conferma}">
						<th width="5%" class="etichetta" align="center" scope="col"
							bgcolor="#dde8f0"><bean:message
							key="servizi.utenti.headerSelezionataMultipla"
							bundle="serviziLabels" /></th>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</tr>
			<logic:iterate id="listaServizi" property="autAna.elencoServizi"
				name="navForm" indexId="index">
				<bs:define id="vedi">
					<bs:write name="navForm"
						property='<%="autAna.listaServizi["
											+ index + "].stato"%>' />
				</bs:define>
				<c:choose>
					<c:when test='${vedi != "3"}'>
						<sbn:rowcolor var="color" index="index" />
						<tr><%--
							<td bgcolor="${color}" class="testoNoBold" width="4%"><bs:write
								name="navForm"
								property='<%="autAna.listaServizi["
												+ index + "].codBiblioteca"%>' /></td>
							--%>
							<td bgcolor="${color}" class="testoNoBold"><bs:write
								name="navForm"
								property='<%="autAna.listaServizi["
												+ index + "].componiTipoServizio"%>' /></td>
							<td bgcolor="${color}" class="testoNoBold"><bs:write
								name="navForm"
								property='<%="autAna.listaServizi["
												+ index + "].componi"%>' /></td>
							<%--
							<td bgcolor="${color}" class="testoNoBold"><bs:write
								name="navForm"
								property='<%="autAna.listaServizi["
												+ index + "].desServizio"%>' /></td>
							--%><c:choose>
								<c:when test="${!navForm.conferma}">

									<td bgcolor="${color}" class="testoNoBold" align="center">
									<html:checkbox styleId="testoNoBold"
										name="navForm"
										property='<%="autAna.listaServizi["
																+ index
																+ "].cancella"%>'
										value="C" /></td>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>

						</tr>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>

			</logic:iterate>
		</table>
		</sbn:disableAll>
		</div>
		<br>
		<div id="divFooter"><c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/autorizzazioni/footerDettAutorizzazioni.jsp" />
<!--						<jsp:include page="/WEB-INF/jsp/subpages/servizi/autorizzazioni/footerNuovaAutorizzazione.jsp" />-->
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
