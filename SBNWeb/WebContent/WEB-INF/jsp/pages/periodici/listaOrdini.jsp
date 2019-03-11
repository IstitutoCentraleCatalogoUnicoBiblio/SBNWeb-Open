<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/listaOrdini.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br />
		<jsp:include
			page="/WEB-INF/jsp/subpages/periodici/intestazionePeriodico.jsp"
			flush="true" /> <sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="blocco.totRighe" parameter="methodOrdine"
			totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe" />

		<table align="center" width="100%" border="0"
			style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
			<tr bgcolor="#dde8f0">
				<td class="etichetta" scope="col" align="center"></td>

				<td class="etichetta" scope="col" align="center"><bean:message
					key="buono.label.tabBibl" bundle="acquisizioniLabels" /></td>
				<td class="etichetta" style="width: 5%;" scope="col" align="center">
				<bean:message key="buono.label.anno" bundle="acquisizioniLabels" />
				</td>

				<td class="etichetta" style="width: 5%;" scope="col" align="center">
				<bean:message key="ordine.label.tabTipo" bundle="acquisizioniLabels" />
				</td>

				<td class="etichetta" scope="col" style="width: 3%;" align="center">
					<bean:message key="ordine.label.tabNum" bundle="acquisizioniLabels" />
				</td>

				<td class="etichetta" scope="col" align="center"><bean:message
					key="buono.label.dataBuono" bundle="acquisizioniLabels" /></td>


				<td class="etichetta" title="Stampato" style="width: 2%;"
					scope="col" align="center"><bean:message
					key="buono.label.tabStato" bundle="acquisizioniLabels" /></td>

				<td class="etichetta" title="Stato" style="width: 5%;" scope="col"
					align="center"><bean:message key="ordine.label.stato"
					bundle="acquisizioniLabels" /></td>

				<td class="etichetta" style="width: 15%;" scope="col" align="center">
				<bean:message key="ordine.label.bid" bundle="acquisizioniLabels" />
				</td>
				<td class="etichetta" style="width: 30%;" scope="col" align="center">
				<bean:message key="ordine.label.tabTitolo"
					bundle="acquisizioniLabels" /></td>
				<td class="etichetta" title="Natura" scope="col" align="center">
				<bean:message key="ordine.label.tabNatura"
					bundle="acquisizioniLabels" /></td>
				<td class="etichetta" title="Continuativo" scope="col"
					align="center"><bean:message
					key="ordine.label.tabContinuativo" bundle="acquisizioniLabels" />
				</td>
				<td class="etichetta" title="Rinnovato" scope="col" align="center">
				<bean:message key="ordine.label.tabRinnovato"
					bundle="acquisizioniLabels" /></td>
				<td class="etichetta" colspan="2" style="width: 30%;" scope="col"
					align="center"><bean:message key="ordine.label.fornitore"
					bundle="acquisizioniLabels" /></td>

				<c:if test="${navForm.bilancioAttivo}">
					<td class="etichetta" scope="col" align="center" colspan="3">
					<bean:message key="buono.label.tabBilancio"
						bundle="acquisizioniLabels" /></td>
				</c:if>
				<td class="etichetta" scope="col" align="center" style="width: 3%;"></td>
			</tr>

			<logic:iterate id="item" property="listaOrdini" name="navForm" indexId="idx">
				<sbn:rowcolor var="color" index="idx" />
				<tr class="testoNormale" bgcolor="${color}">
					<td>
					<div align="center" class="testoNormale"><sbn:linkbutton
						name="item" index="uniqueId" value="progr" property="selected"
						key="button.periodici.scegli" bundle="periodiciLabels" /></div>
					</td>
					<td align="center"><bs:write name="item"
						property="ordine.codBibl" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.annoOrdine" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.tipoOrdine" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.codOrdine" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.dataOrdine" /></td>
					<td align="center"><html:checkbox name="item"
						property="ordine.stampato" disabled="true" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.statoOrdine" /></td>
					<td align="center" style="width: 5%;"><bs:write name="item"
						property="ordine.titolo.codice" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.titolo.descrizione" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.naturaOrdine" /></td>
					<td scope="col"><html:checkbox name="item"
						property="ordine.continuativo" disabled="true" /></td>
					<td scope="col"><html:checkbox name="item"
						property="ordine.rinnovato" disabled="true" /></td>
					<td align="center" style="width: 5%;"><bs:write name="item"
						property="ordine.fornitore.codice" /></td>
					<td align="center"><bs:write name="item"
						property="ordine.fornitore.descrizione" /></td>
					<c:if test="${navForm.bilancioAttivo}">
						<c:choose>
							<c:when test="${item.bilancioPresente}">
								<td align="center"><bs:write name="item"
									property="ordine.bilancio.codice1" /></td>
								<td align="center"><bs:write name="item"
									property="ordine.bilancio.codice2" /></td>
								<td align="center"><bs:write name="item"
									property="ordine.bilancio.codice3" /></td>
							</c:when>
							<c:otherwise>
								<td align="center">&nbsp;</td>
								<td align="center">&nbsp;</td>
								<td align="center">&nbsp;</td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<td>
					<div class="testoNormale"><html:radio name="navForm"
						property="selected" value="${item.uniqueId}" /></div>
					</td>
				</tr>
			</logic:iterate>

		</table>
		</div>
		<div id="divFooterCommon">
		<sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="blocco.totRighe" parameter="methodOrdine"
			totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe" bottom="true" />
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="pulsanti" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>


