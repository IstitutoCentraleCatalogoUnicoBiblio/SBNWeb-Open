<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<div id="divForm"><sbn:navform
		action="/amministrazionesistema/sinteticaBibliotecario.do">
		<div id="divMessaggio"><sbn:errors /></div>

		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodSinBibliotecario" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe"></sbn:blocchi>

		<bean-struts:define id="color" value="#FEF1E2" />
		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="24%"><html:link
					page="/amministrazionesistema/sinteticaBibliotecario.do?cmd=cognome">
					<bean:message key="ricerca.bibliotecario.cognome"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="24%"><html:link
					page="/amministrazionesistema/sinteticaBibliotecario.do?cmd=nome">
					<bean:message key="ricerca.bibliotecario.nome"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="10%"><html:link
					page="/amministrazionesistema/sinteticaBibliotecario.do?cmd=username">
					<bean:message key="ricerca.bibliotecario.username"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="20%"><bean:message
					key="ricerca.bibliotecario.biblioteche"
					bundle="amministrazioneSistemaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="6%"><bean:message
					key="ricerca.bibliotecario.abilitato"
					bundle="amministrazioneSistemaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="14%"><bean:message
					key="ricerca.bibliotecario.dataaccesso.sin"
					bundle="amministrazioneSistemaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="2%"></th>
			</tr>

			<logic:iterate id="item" property="elencoUtenti"
				name="sinteticaBibliotecarioForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td  style="text-align: left"><c:out
						value="${item.cognome}"></c:out></td>
					<td  style="text-align: left"><c:out
						value="${item.nome}"></c:out></td>
					<td  style="text-align: left"><c:out
						value="${item.username}"></c:out></td>
					<td  style="text-align: left"><c:out
						value="${item.biblioteca}"></c:out></td>
					<td class="testoBold" style="text-align: center"><c:out
						value="${item.abilitato}"></c:out></td>
					<td  style="text-align: center"><c:out
						value="${item.dataAccesso}"></c:out></td>
					<td  style="text-align: center"><html:radio
						property="selezRadio" value="${item.id}"></html:radio></td>
				</tr>
			</logic:iterate>
		</table>


		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodSinBibliotecario" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>

		<br />

		<div id="divFooter">
		<table border="0" style="height: 40px" align="center">
			<tr>
				<td width="100%"><html:submit styleClass="pulsanti"
					property="methodSinBibliotecario">
					<bean:message key="ricerca.bibliotecario.button.indietro"
						bundle="amministrazioneSistemaLabels" />
				</html:submit> <c:choose>
					<c:when
						test="${sinteticaBibliotecarioForm.acquisizioni eq 'FALSE'  or (sinteticaBibliotecarioForm.stampaEtichette eq 'FALSE')}">
						<c:choose>
							<c:when
								test="${sinteticaBibliotecarioForm.abilitatoNuovo eq 'TRUE'}">
								<html:submit styleClass="pulsanti"
									property="methodSinBibliotecario">
									<bean:message key="ricerca.bibliotecario.button.nuovo"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti"
									property="methodSinBibliotecario">
									<bean:message key="ricerca.bibliotecario.button.modifica"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit styleClass="pulsanti"
									property="methodSinBibliotecario">
									<bean:message key="ricerca.bibliotecario.button.scheda"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when
								test="${sinteticaBibliotecarioForm.abilitatoProfiloRead eq 'TRUE'}">
								<html:submit styleClass="pulsanti"
									property="methodSinBibliotecario" disabled="false">
									<bean:message key="ricerca.bibliotecario.button.profilo"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
							<c:when
								test="${sinteticaBibliotecarioForm.abilitatoProfiloWrite eq 'TRUE'}">
								<html:submit styleClass="pulsanti"
									property="methodSinBibliotecario" disabled="false">
									<bean:message key="ricerca.bibliotecario.button.abilitazioni"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti"
							property="methodSinBibliotecario" disabled="false">
							<bean:message key="ricerca.bibliotecario.button.scegli"
								bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
	</div>
</layout:page>