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
		action="/amministrazionesistema/sinteticaBiblioteca.do">
		<div id="divMessaggio"><sbn:errors /></div>

		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodSinBiblioteca" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe"></sbn:blocchi>

		<bean-struts:define id="color" value="#FEF1E2" />

		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="4%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=polo">
					<bean:message key="nuovo.biblioteca.polo"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="5%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=codBib">
					<bean:message key="ricerca.biblioteca.cdbib"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="5%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=codAna">
					<bean:message key="ricerca.biblioteca.cdana"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="23%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=nome">
					<bean:message key="ricerca.biblioteca.nome"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="23%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=indirizzo">
					<bean:message key="ricerca.biblioteca.indirizzo"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="14%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=tipo">
					<bean:message key="ricerca.biblioteca.tipo"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="3%"><html:link
					page="/amministrazionesistema/sinteticaBiblioteca.do?cmd=flag">
					<bean:message key="ricerca.biblioteca.flag"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="2%"></th>
			</tr>
			<logic:iterate id="item" property="elencoBiblioteche"
				name="navForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td  style="text-align: center">
						<sbn:anchor name="item" property="prg"/>
						<c:out value="${item.cod_polo}"></c:out></td>
					<td  style="text-align: center"><c:out
						value="${item.cod_bib}"></c:out></td>
					<td  style="text-align: center"><c:out
						value="${item.cd_ana_biblioteca}"></c:out></td>
					<td  style="text-align: left"><c:out
						value="${item.nom_biblioteca}"></c:out></td>
					<td  style="text-align: left"><c:out
						value="${item.indirizzoComposto}"></c:out></td>
					<td  style="text-align: left"><c:out
						value="${item.tipo_biblioteca}"></c:out></td>
					<td  style="text-align: center">
						<c:choose>
							<%--
							<c:when test="${item.flag_bib eq 'N'}">
								<bean:message key="profilo.biblioteca.button.no" bundle="amministrazioneSistemaLabels"/>
							</c:when>
							<c:when test="${item.flag_bib eq 'D'}">
								<bean:message key="profilo.biblioteca.flag.cds.noaff" bundle="amministrazioneSistemaLabels"/>
							</c:when>
							--%>
							<c:when test="${item.cod_polo ne sessionScope.UTENTE_KEY.codPolo}">
								&nbsp;
							</c:when>
							<c:when test="${item.flag_bib eq 'C'}">
								<bean:message key="profilo.biblioteca.button.si" bundle="amministrazioneSistemaLabels"/>
							</c:when>
							<c:when test="${item.flag_bib eq 'A'}">
								<bean:message key="ricerca.biblioteca.flag.aff" bundle="amministrazioneSistemaLabels"/>
							</c:when>
						</c:choose>
					</td>
					<td  style="text-align: center"><html:radio
						property="selezRadio" value="${item.idBiblioteca}"></html:radio></td>
				</tr>
			</logic:iterate>
		</table>

		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodSinBiblioteca" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>

		<br />

		<div id="divFooter">
		<table border="0" style="height: 40px" align="center">
			<tr>
				<td width="100%"><html:submit styleClass="pulsanti"
					property="methodSinBiblioteca">
					<bean:message key="ricerca.biblioteca.button.indietro"
						bundle="amministrazioneSistemaLabels" />
				</html:submit>
				<sbn:checkAttivita idControllo="SERVIZI_ILL">
					<html:submit styleClass="pulsanti" property="methodSinBiblioteca">
						<bean:message key="ricerca.biblioteca.button.nuovo"	bundle="amministrazioneSistemaLabels" />
					</html:submit>
				</sbn:checkAttivita>
				<c:choose>
					<c:when test="${(navForm.acquisizioni eq 'FALSE') or (navForm.scaricoInventariale eq 'FALSE') }">
						<c:choose>
							<c:when
								test="${navForm.abilitatoNuovo eq 'TRUE'}">
								<html:submit styleClass="pulsanti"
									property="methodSinBiblioteca">
									<bean:message key="ricerca.biblioteca.button.nuovo"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti"
									property="methodSinBiblioteca">
									<bean:message key="ricerca.biblioteca.button.modifica"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit styleClass="pulsanti"
									property="methodSinBiblioteca">
									<bean:message key="ricerca.biblioteca.button.scheda"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when
								test="${navForm.abilitatoProfiloRead eq 'TRUE'}">
								<html:submit styleClass="pulsanti"
									property="methodSinBiblioteca" disabled="false">
									<bean:message key="ricerca.biblioteca.button.profilo"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
							<c:when
								test="${navForm.abilitatoProfiloWrite eq 'TRUE'}">
								<html:submit styleClass="pulsanti"
									property="methodSinBiblioteca" disabled="false">
									<bean:message key="ricerca.biblioteca.button.abilitazioni"
										bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti" property="methodSinBiblioteca"
							disabled="false">
							<bean:message key="ricerca.biblioteca.button.scegli"
								bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
		</div>

	</sbn:navform></div>
</layout:page>