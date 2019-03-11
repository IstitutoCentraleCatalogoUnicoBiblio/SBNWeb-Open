<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>

	<sbn:navform
		action="/gestionesemantica/classificazione/TrascinaTitoliClassi.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br>
		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				&nbsp;</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.sistema"
						bundle="gestioneSemanticaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.edizione"
						bundle="gestioneSemanticaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.simbolo"
						bundle="gestioneSemanticaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				<bean:message key="sintetica.descrizione"
					bundle="gestioneSemanticaLabels" /></th>
			</tr>
			<bean-struts:define id="color" value="#FEF1E2" />
			<logic:iterate id="listaTermini" property="classi"
				name="TrascinaTitoliClassiForm" indexId="progr">
				<sbn:rowcolor var="color" index="progr" />
				<tr bgcolor="${color}">
					<td class="testoBold" align="right"><c:choose>
						<c:when test="${progr eq 0}">
							<bean:message key="gestionesemantica.idPartenza"
								bundle="gestioneSemanticaLabels" />
						</c:when>
						<c:otherwise>
							<bean:message key="gestionesemantica.idArrivo"
								bundle="gestioneSemanticaLabels" />
						</c:otherwise>
					</c:choose></td>
					<td ><bean-struts:write name="listaTermini"
						property="simboloDewey.sistema" /></td>
					<td ><bean-struts:write name="listaTermini"
						property="simboloDewey.edizione" /></td>
					<td ><bean-struts:write name="listaTermini"
						property="simboloDewey.simbolo" /></td>
					<td ><bean-struts:write name="listaTermini"
						property="descrizione" /></td>
				</tr>
			</logic:iterate>
		</table>
		<br>
		<table width="100%" border="0">
			<tr>
				<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
					parameter="methodTraTitCla" totBlocchi="totBlocchi"
					elementiPerBlocco="maxRighe" livelloRicerca="P" />
			</tr>
		</table>
		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"><bean:message
					key="trascina.progr" bundle="gestioneSemanticaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"><bean:message
					key="trascina.bid" bundle="gestioneSemanticaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"><bean:message
					key="trascina.headerStato" bundle="gestioneSemanticaLabels" /></th>
				<!--
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.headerUtilizzato"
							bundle="gestioneSemanticaLabels" />
					</th>
					-->
				<th class="etichetta" scope="col" bgcolor="#dde8f0"><bean:message
					key="trascina.isbd" bundle="gestioneSemanticaLabels" /></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
			</tr>
			<logic:iterate id="item" property="listaSintetica"
				name="TrascinaTitoliClassiForm" indexId="progr">
				<sbn:rowcolor var="color" index="progr" />
				<tr bgcolor="${color}" class="testoNormale">
					<td>
						<bean-struts:write name="item" property="progr" />
						<sbn:anchor name="item" property="progr" />
					</td>
					<td><bean-struts:write name="item" property="bid" /></td>
					<td><bean-struts:write name="item" property="stato" /></td>
					<!--<td><bean-struts:write name="item" property="utilizzato" /></td>
					-->
					<td><bean-struts:write name="item" property="isbdELegami" />
					</td>
					<td><html:checkbox name="item" property="selezBox"
						value="${progr}" indexed="true"
						disabled="${TrascinaTitoliClassiForm.enableConferma}" /></td>
				</tr>
			</logic:iterate>
		</table>
		</div>
		<div id="divFooterCommon"><sbn:blocchi numBlocco="numBlocco"
			numNotizie="totRighe" parameter="methodTraTitCla"
			totBlocchi="totBlocchi" elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>
		</div>
		<div id="divFooter"><c:choose>
			<c:when test="${TrascinaTitoliClassiForm.enableConferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/classificazione/bottonieraTrascinaCla.jsp" />
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
