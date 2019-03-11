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
		action="/gestionesemantica/thesauro/SinteticaTitoliThesauro.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<br />
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						&nbsp;
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.did" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestionesemantica.thesauro.thesauro"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.headerStato"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestione.formaNome"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.termine"
							bundle="gestioneSemanticaLabels" />
					</th>
				</tr>
				<bean-struts:define id="color" value="#FEF1E2" />
				<logic:iterate id="listaTermini" property="termini"
					name="SinteticaTitoliThesauroForm" indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr bgcolor="${color}">
						<td class="testoBold" align="right">
							<c:choose>
								<c:when test="${progr eq 0}">
									<bean:message key="gestionesemantica.idPartenza" bundle="gestioneSemanticaLabels" />
								</c:when>
								<c:otherwise>
									<bean:message key="gestionesemantica.idArrivo" bundle="gestioneSemanticaLabels" />
								</c:otherwise>
							</c:choose>
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="did" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="codThesauro" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="livAut" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="formaNome" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="testo" />
						</td>
					</tr>
				</logic:iterate>
			</table>
			<br />
			<sbn:blocchi numBlocco="numBlocco"
				numNotizie="datiLegame.titoliCollegati.totRighe"
				parameter="methodSintTitThe"
				totBlocchi="datiLegame.titoliCollegati.totBlocchi"
				elementiPerBlocco="datiLegame.titoliCollegati.maxRighe"
				livelloRicerca="P" />
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
				<!--<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.headerUtilizzato"
							bundle="gestioneSemanticaLabels" />
					</th>
					-->
				<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="trascina.isbd" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<logic:iterate id="item"
					property="datiLegame.titoliCollegati.listaSintetica"
					name="SinteticaTitoliThesauroForm" indexId="progr">
					<sbn:rowcolor var="color" index="progr"></sbn:rowcolor>
					<tr bgcolor="${color}">
						<td>
							<bean-struts:write name="item" property="progressivo" />
						</td>
						<td>
							<bean-struts:write name="item" property="bid" />
						</td>
						<td>
							<bean-struts:write name="item" property="livelloAutorita" />
						</td>
					<!--
						<td>
							<bean-struts:write name="item" property="livelloAutorita" />
						</td>
						-->
					<td>
							<bean-struts:write name="item" property="descrizioneLegami"
								filter="false" />
						</td>
						<td>
							<html:multibox name="SinteticaTitoliThesauroForm"
								property="titoliSelezionati" value="${item.bid}"
								disabled="${SinteticaTitoliThesauroForm.enableConferma or
									SinteticaTitoliThesauroForm.fusione}" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco"
				numNotizie="datiLegame.titoliCollegati.totRighe"
				parameter="methodSintTitThe"
				totBlocchi="datiLegame.titoliCollegati.totBlocchi"
				elementiPerBlocco="datiLegame.titoliCollegati.maxRighe"
				bottom="true" />
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td>
						<sbn:bottoniera buttons="listaPulsanti" />
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
