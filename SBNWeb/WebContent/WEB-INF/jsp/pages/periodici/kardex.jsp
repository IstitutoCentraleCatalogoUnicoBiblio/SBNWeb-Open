<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/kardexPeriodici.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br />
		<c:if test="${ (navForm.operazione eq 'ASSOCIA_MULTIPLA_INVENTARIO' or navForm.operazione eq 'ASSOCIA_MULTIPLA_GRUPPO') and navForm.conferma }">
			<table width="99%" class="sintetica">
			<tr><td align="center">
				<span class="testoBold"><bean:message key="periodici.kardex.associa.partizione.inv" bundle="periodiciLabels" />&nbsp;</span>
				<html:text name="navForm" property="inventario.gruppoFascicolo" maxlength="4" size="4" />
			</td></tr>
			</table>
			<br />
			<hr />
		</c:if>
		<jsp:include page="/WEB-INF/jsp/subpages/periodici/kardexIntestazione.jsp" flush="true" />
		<hr />
		<table width="99%">
			<tr>
				<td><sbn:blocchi numBlocco="bloccoSelezionato"
					numNotizie="kardex.blocco.totRighe" parameter="methodKardex"
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
				<c:if test="${navForm.operazione ne 'SCEGLI_FASCICOLI_PER_CREA_ORDINE'}">
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="periodici.kardex.stato" bundle="periodiciLabels" />
					</th>
					<c:if test="${navForm.kardex.tipo ne 'COLLOCAZIONE'}">
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="periodici.esame.collocazione" bundle="periodiciLabels" />
						</th>
					</c:if>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<c:choose>
							<c:when test="${navForm.kardex.tipo eq 'ORDINE'}">
								<bean:message key="periodici.kardex.data.ric" bundle="periodiciLabels" />&nbsp;
							</c:when>
							<c:otherwise>
								<bean:message key="periodici.kardex.data.associa" bundle="periodiciLabels" />&nbsp;
							</c:otherwise>
						</c:choose>
					</th>
				</c:if>
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
				<th bgcolor="#dde8f0" align="center">&nbsp;</th>
			</tr>
			<logic:iterate id="item" property="fascicoli" name="kardexForm" indexId="idx">
				<sbn:rowcolor var="color" index="idx" />
				<tr bgcolor="${color}">
					<td>
						<sbn:anchor name="item" property="progr"/>
						<sbn:linkbutton name="item" index="repeatableId" value="numFascicolo"
							property="selected" key="button.periodici.esamina" bundle="periodiciLabels"
							disabled="${navForm.conferma}" checkAttivita="LINK_NUM_FASCICOLO" withAnchor="false" />
					</td>
					<td><bs:write name="item" property="data_conv_pub" format="dd/MM/yyyy" /></td>
					<c:if test="${navForm.operazione ne 'SCEGLI_FASCICOLI_PER_CREA_ORDINE'}">
						<td>
							<sbn:linkbutton name="item" index="repeatableId" value="descrizioneStato"
								property="selected" key="button.periodici.esamina.sollecito" bundle="periodiciLabels"
								disabled="${navForm.conferma or not item.sollecitato}" checkAttivita="LINK_STATO_FASCICOLO" />
							<c:if test="${item.posseduto}">
								&nbsp;
								<c:choose>
									<c:when test="${navForm.kardex.tipo ne 'ORDINE'}">
										<c:if test="${not empty item.dettaglioRicezione}">
											&#40;<bs:write name="item" property="dettaglioRicezione" />&#41;
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if test="${not empty item.dettaglioRicezioneSenzaOrdine}">
											&#40;<bs:write name="item" property="dettaglioRicezioneSenzaOrdine" />&#41;
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<c:if test="${navForm.kardex.tipo ne 'COLLOCAZIONE'}">
							<td><bs:write name="item" property="chiaveCollocazione" /></td>
						</c:if>
						<td>
							<c:if test="${item.posseduto}">
								<bs:write name="item" property="esemplare.data_arrivo" format="dd/MM/yyyy" />
							</c:if>
						</td>
					</c:if>
					<td><bs:write name="item" property="annata"/></td>
					<td><bs:write name="item" property="numVolume"/></td>
					<td><bs:write name="item" property="tipoFascicolo"/></td>
					<td><bs:write name="item" property="periodicita"/></td>
					<%-- <td><html:radio property="selected" value="${item.repeatableId}" /></td>  --%>
					<td>
						<html:multibox property="selected" value="${item.repeatableId}" disabled="${navForm.conferma}" />
						<html:hidden property="selected" value="0" disabled="${navForm.conferma}" />
					</td>
				</tr>
			</logic:iterate>
		</table>

		</div>
		<div id="divFooterCommon">
			<table width="99%">
				<tr>
					<td><sbn:blocchi numBlocco="bloccoSelezionato"
						numNotizie="kardex.blocco.totRighe" parameter="methodKardex"
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
				<c:if test="${!navForm.conferma}">
					<c:if test="${navForm.operazione ne 'SCEGLI_FASCICOLI_PER_CREA_ORDINE'}">
						<td><bean:message key="periodici.esame.range.fascicoli" bundle="periodiciLabels" />&#58;&nbsp;
							<bean:message key="periodici.kardex.posiziona.da" bundle="periodiciLabels" />&nbsp;
							<html:text property="ricercaKardex.dataFrom" size="10" maxlength="10" />
						</td>
						<td>
							<bean:message key="periodici.kardex.posiziona.a" bundle="periodiciLabels" />&nbsp;
							<html:text property="ricercaKardex.dataTo" size="10" maxlength="10" />
						</td>
						<td><html:submit property="methodKardex">
								<bean:message key="button.periodici.esegui" bundle="periodiciLabels" />
							</html:submit>
						</td>
						<c:if test="${navForm.kardex.tipo ne 'ORDINE'}">
							<td>
								<bean:message key="periodici.kardex.annate.filtro" bundle="periodiciLabels" />
								<html:checkbox property="ricercaKardex.soloAnnatePossedute" />
								<html:hidden property="ricercaKardex.soloAnnatePossedute" value="false" />
							</td>
						</c:if>
					</c:if>
				</c:if>
			</tr>
		</table>
		<table align="center">
			<tr>
				<td>
					<sbn:bottoniera buttons="pulsanti" />
				</td>
				<c:if test="${!navForm.conferma}">
					<c:if test="${navForm.operazione ne 'SCEGLI_FASCICOLI_PER_CREA_ORDINE'}">
						<td>
						<sbn:checkAttivita idControllo="POSIZIONA">
							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodKardex" title="Seleziona tutto">
									<bean:message key="button.selAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodKardex" title="Deseleziona tutto">
									<bean:message key="button.deSelAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</sbn:checkAttivita>
					</c:if>
				</c:if>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>


