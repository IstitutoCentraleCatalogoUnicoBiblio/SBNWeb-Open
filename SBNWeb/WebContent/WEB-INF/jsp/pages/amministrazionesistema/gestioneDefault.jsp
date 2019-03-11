<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>


<html:xhtml/>
<layout:page>

<sbn:navform action="/amministrazionesistema/gestioneDefault.do">

		<div id="divForm">
			<div id="divMessaggio">
					<sbn:errors />
			</div>
		<table border="0">
			<tr>
				<td width=800 align="center" style="font-weight: bold; font-size: 15px">
					<bean:message key="profilo.gestione.default.intestazione" bundle="amministrazioneSistemaLabels"/>
					<bean:message key="${navForm.idarea}" bundle="amministrazioneSistemaLabels"/>
				</td>
			</tr>
		</table>

		<br/>

			<logic:iterate id="item" property="campi" name="navForm">
						<bean:message key="${item.nome}" bundle="${item.bundle}" />:
						<table border = "0" cellspacing="15">
							<tr>
								<th width="130" align="left">
               					<bean:message  key="profilo.gestione.default.etichetta" bundle="amministrazioneSistemaLabels" />
								<th width="213" align="left">
               					<c:choose>
               						<c:when test="${navForm.utilizzo eq 'utente'}">
		               					<bean:message  key="profilo.gestione.default.biblioteca" bundle="amministrazioneSistemaLabels" />
               						</c:when>
               						<c:when test="${navForm.utilizzo eq 'biblioteca'}">
        		       					<bean:message  key="profilo.gestione.default.sistema" bundle="amministrazioneSistemaLabels" />
               						</c:when>
               					</c:choose>


								</th>
								<th width="90" align="left">
               					<bean:message  key="profilo.gestione.default.provenienza" bundle="amministrazioneSistemaLabels" />
								</th>
								<th width="301" align="left">
               					<c:choose>
               						<c:when test="${navForm.utilizzo eq 'utente'}">
		               					<bean:message  key="profilo.gestione.default.personali" bundle="amministrazioneSistemaLabels" />
               						</c:when>
               						<c:when test="${navForm.utilizzo eq 'biblioteca'}">
		               					<bean:message  key="profilo.gestione.default.biblioteca" bundle="amministrazioneSistemaLabels" />
               						</c:when>
               					</c:choose>
								</th>
							</tr>
						</table>

					<table border="0" cellspacing="15">

					<logic:iterate id="elem" property="def" name="item">
						<c:set var="defaultVO" scope="request" value="${elem}" />
							<tr>
								<td width="130" align="left">
									<c:if test="${elem.nome != ''}">
										<bean:message key="${elem.nome}" bundle="${elem.bundle}"/>:</td>
									</c:if>
								<td width="211" align="left">
									<c:choose>
										<c:when test="${elem.tipo eq 'multiOpzioni'}">
											<html:select property='campi[${item.indice}].def[${elem.indice}].defaultSistemaMulti' multiple="true" disabled="true">
												<html:optionsCollection property="campi[${item.indice}].def[${elem.indice}].listaOpzioni" value="descrizione" label="descrizione"/>
											</html:select>
										</c:when>
										<c:otherwise>
											<html:text property="campi[${item.indice}].def[${elem.indice}].defaultSistema" size="31" disabled="true"></html:text>
										</c:otherwise>
									</c:choose>
								</td>
							    <td width="90" align="left">
							    <c:choose>
	  								<c:when test="${elem.provenienza eq 'BIBLIOTECARIO'}">
               							<bean:message key="profilo.default.bibliotecario" bundle="amministrazioneSistemaLabels" />
               						</c:when>
	  								<c:when test="${elem.provenienza eq 'POLO'}">
               							<bean:message key="profilo.default.polo" bundle="amministrazioneSistemaLabels" />
               						</c:when>
	  								<c:when test="${elem.provenienza eq 'BIBLIOTECA'}">
               							<bean:message key="profilo.default.biblioteca" bundle="amministrazioneSistemaLabels" />
               						</c:when>
	  								<c:when test="${elem.provenienza eq 'SISTEMA'}">
               							<bean:message key="profilo.default.sistema" bundle="amministrazioneSistemaLabels" />
               						</c:when>
               					</c:choose> </td>
								<td width="301" align="left">
									<sbn:disableAll checkAttivita="EDITOR">
										<c:choose>
											<c:when test="${elem.tipo eq 'String'}">
												<html:text property='campi[${item.indice}].def[${elem.indice}].selezione' value="${elem.selezione}" size="${elem.dimensione}" maxlength="${elem.dimensione}" /></td> <!-- MAX SIZE PER QUESTO CAMPO: 45 -->
											</c:when>
											<c:when test="${elem.tipo eq 'Opzioni'}">
												<html:select property='campi[${item.indice}].def[${elem.indice}].selezione' size="${elem.dimensione}">
													<html:optionsCollection property="campi[${item.indice}].def[${elem.indice}].listaOpzioni" value="codice" label="descrizione"/>
												</html:select>
											</c:when>
											<c:when test="${elem.tipo eq 'multiOpzioni'}">
												<html:select property='campi[${item.indice}].def[${elem.indice}].selezioneMulti' multiple="true" size="${elem.dimensione}">
													<html:optionsCollection property="campi[${item.indice}].def[${elem.indice}].listaOpzioni" value="codice" label="descrizione"/>
												</html:select>
											</c:when>
										</c:choose>
									</sbn:disableAll>
								</td>

               				</tr>

               		</logic:iterate>

               		</table>

		<hr>

			</logic:iterate>

		<div id="divFooter">
			<table align="center" border="0" style="height:40px">
				<tr>
					<td>
						<html:submit styleClass="pulsanti" property="methodDefault">
							<bean:message key="profilo.button.salva" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodDefault">
							<bean:message key="profilo.button.annulla" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>

		</div>

	</sbn:navform>
</layout:page>
