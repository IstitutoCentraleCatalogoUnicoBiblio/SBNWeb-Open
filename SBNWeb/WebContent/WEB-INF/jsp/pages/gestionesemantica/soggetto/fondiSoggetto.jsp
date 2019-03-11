<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/FondiSoggetto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<br>
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						&nbsp;
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.cid" bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.testo"
							bundle="gestioneSemanticaLabels" />
					</th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
				<l:iterate id="listaTermini" property="soggetti"
					name="FondiSoggettoForm" indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr bgcolor="${color}">
						<td class="testoBold" align="right">
							<c:choose>
								<c:when test="${progr eq 0}">
									<bean:message key="gestionesemantica.idPartenza"
										bundle="gestioneSemanticaLabels" />
								</c:when>
								<c:otherwise>
									<bean:message key="gestionesemantica.idArrivo"
										bundle="gestioneSemanticaLabels" />
								</c:otherwise>
							</c:choose>
						</td>
						<td >
							<bs:write name="listaTermini" property="cid" />
						</td>
						<td >
							<bs:write name="listaTermini" property="testo" />
						</td>
					</tr>
				</l:iterate>
			</table>
			<br>

			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodFondiSog" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" livelloRicerca="P" />

			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.progr"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.bid" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.headerStato"
							bundle="gestioneSemanticaLabels" />
					</th>
					<!-- <th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.headerUtilizzato"
							bundle="gestioneSemanticaLabels" />
					</th> -->
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.isbd" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<l:notEmpty name="FondiSoggettoForm" property="listaSintetica">
					<l:iterate id="item" property="listaSintetica"
						name="FondiSoggettoForm" indexId="progr">
						<sbn:rowcolor var="color" index="progr"/>
						<tr class="testoNormale" bgcolor="${color}">
							<td>
								<sbn:anchor name="item" property="progr" />
								<bs:write name="item" property="progr" />
							</td>
							<td>
								<bs:write name="item" property="bid" />
							</td>
							<td>
								<bs:write name="item" property="stato" />
							</td>
							<%--<td>
								<bs:write name="item" property="utilizzato" />
							</td> --%>
							<td>
								<bs:write name="item" property="isbdELegami"
									filter="false" />
							</td>
							<td>
								<html:checkbox name="item" property="selezBox" value="${progr}"
									indexed="true" disabled="true" /> <%--${FondiSoggettoForm.enableConferma}" /> --%>
							</td>
						</tr>
					</l:iterate>
				</l:notEmpty>
			</table>
		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodFondiSog" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true" />
		</div>
		<div id="divFooter">
			<c:choose>
				<c:when test="${FondiSoggettoForm.enableConferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/bottonieraFondi.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
