<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/esameKardex.do">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

		</div>
		<jsp:include page="/WEB-INF/jsp/subpages/periodici/kardexIntestazione.jsp" flush="true" />
		<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" flush="true" />
		<hr />
		<table width="99%">
			<tr>
				<td><sbn:blocchi numBlocco="bloccoSelezionato"
					numNotizie="kardex.blocco.totRighe" parameter="methodEsame"
					totBlocchi="kardex.blocco.totBlocchi" elementiPerBlocco="kardex.blocco.maxRighe"
					disabled="${navForm.conferma}" /></td>
				<td align="right" id="posiziona">
					<sbn:checkAttivita idControllo="POSIZIONA">
						<sbn:disableAll disabled="${navForm.conferma}">
							<bean:message key="periodici.kardex.posiziona" bundle="periodiciLabels" />
							&nbsp;<span style="font-size: 75%;">gg/mm/aaaa</span>&nbsp;
							<html:text property="posizionaTop" size="10" maxlength="10" />
							<html:submit property="${sbn:getUniqueButtonName(navForm.SUBMIT_BUTTON_POSIZIONA, 'false')}">
								<bean:message key="button.periodici.posiziona" bundle="periodiciLabels" />
							</html:submit>
						</sbn:disableAll>
					</sbn:checkAttivita>
				</td>
			</tr>
		</table>
		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.fascicolo" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.data.conv.fasc" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.annata" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.volume" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.tipo_fasc" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.tipo.per" bundle="periodiciLabels" />
				</th>
				<c:choose>
				<c:when test="${navForm.operazione eq 'ESAME'}">
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="periodici.kardex.esempl.bib" bundle="periodiciLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="periodici.kardex.esempl.polo" bundle="periodiciLabels" />
					</th>
				</c:when>
				<c:when test="${navForm.operazione eq 'PREVISIONALE'}">
					<th class="etichetta minimal" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="periodici.previsionale.exists" bundle="periodiciLabels" />
					</th>
				</c:when>
				<c:otherwise>
				</c:otherwise>
				</c:choose>
				<th bgcolor="#dde8f0" align="center">&nbsp;</th>
			</tr>
			<logic:iterate id="item" property="fascicoli" name="navForm" indexId="idx">
				<sbn:rowcolor var="color" index="idx" />
				<tr bgcolor="${color}">
					<td>
						<sbn:anchor name="item" property="progr"/>
						<sbn:linkbutton name="item" index="repeatableId" value="numFascicolo"
							property="selected" key="button.periodici.esamina" bundle="periodiciLabels"
							disabled="${navForm.conferma}" checkAttivita="LINK_NUM_FASCICOLO" withAnchor="false" />
						<!-- &nbsp;&#40;<bs:write name="item" property="fid" />&#41; -->
					</td>
					<td><bs:write name="item" property="data_conv_pub" format="dd/MM/yyyy" /></td>
					<td><bs:write name="item" property="annata"/></td>
					<td><bs:write name="item" property="numVolume"/></td>
					<td><bs:write name="item" property="tipoFascicolo"/></td>
					<td><bs:write name="item" property="periodicita"/></td>
					<c:choose>
					<c:when test="${navForm.operazione eq 'ESAME'}">
						<td>
							<sbn:linkbutton name="item" index="repeatableId" value="numEsemplariBib"
								property="selected" key="button.periodici.biblio" bundle="periodiciLabels"
								disabled="${item.numEsemplariBib lt 1}" />
						</td>
						<td>
							<sbn:linkbutton name="item" index="repeatableId" value="numEsemplariPolo"
								property="selected" key="button.periodici.polo" bundle="periodiciLabels"
								disabled="${item.numEsemplariPolo lt 1}" />
						</td>
					</c:when>
					<c:when test="${navForm.operazione eq 'PREVISIONALE'}">
						<td align="center">
							<c:if test="${item.salvato}">
								<html:checkbox name="item" property="salvato" disabled="true" />
							</c:if>
						</td>
					</c:when>
					<c:otherwise>
					</c:otherwise>
					</c:choose>

					<%-- <td><html:radio property="selected" value="${item.repeatableId}" /></td>  --%>
					<td>
						<html:multibox property="selected" value="${item.repeatableId}" disabled="${navForm.conferma}" />
						<html:hidden property="selected" value="0" disabled="${navForm.conferma}" />
					</td>
				</tr>
			</logic:iterate>
		</table>

		<div id="divFooterCommon">
			<table width="99%">
				<tr>
					<td><sbn:blocchi numBlocco="bloccoSelezionato"
						numNotizie="kardex.blocco.totRighe" parameter="methodEsame"
						totBlocchi="kardex.blocco.totBlocchi" elementiPerBlocco="kardex.blocco.maxRighe"
						bottom="true" disabled="${navForm.conferma}" /></td>
					<td align="right" id="posiziona">
						<sbn:checkAttivita idControllo="POSIZIONA_BOTTOM">
							<sbn:disableAll disabled="${navForm.conferma}">
								<bean:message key="periodici.kardex.posiziona" bundle="periodiciLabels" />
								&nbsp;<span style="font-size: 75%;">gg/mm/aaaa</span>&nbsp;
								<html:text property="posizionaBottom" size="10" maxlength="10" />
								<html:submit property="${sbn:getUniqueButtonName(navForm.SUBMIT_BUTTON_POSIZIONA, 'true')}">
									<bean:message key="button.periodici.posiziona" bundle="periodiciLabels" />
								</html:submit>
							</sbn:disableAll>
						</sbn:checkAttivita>
					</td>
				</tr>
			</table>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td>
					<sbn:bottoniera buttons="pulsanti" />
				</td>
				<c:if test="${not navForm.conferma and navForm.operazione ne 'PREVISIONALE'}">
					<td><bean:message key="periodici.esame.range.fascicoli" bundle="periodiciLabels" />&#58;</td>
					<td>
						<bean:message key="periodici.kardex.posiziona.da" bundle="periodiciLabels" />&nbsp;
						<html:text property="ricercaKardex.dataFrom" size="10" maxlength="10" />
					</td>
					<td>
						<bean:message key="periodici.kardex.posiziona.a" bundle="periodiciLabels" />&nbsp;
						<html:text property="ricercaKardex.dataTo" size="10" maxlength="10" />
					</td>
					<td><html:submit property="methodEsame">
						<bean:message key="button.periodici.esegui" bundle="periodiciLabels" />
					</html:submit></td>
				</c:if>
				<sbn:checkAttivita idControllo="POSIZIONA">
					<td align="right">
						<html:submit styleClass="buttonSelezTutti"
							property="methodEsame" title="Seleziona tutto">
							<bean:message key="button.selAllTitoli"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
						<html:submit styleClass="buttonSelezNessuno"
							property="methodEsame" title="Deseleziona tutto">
							<bean:message key="button.deSelAllTitoli"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
				</sbn:checkAttivita>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>


