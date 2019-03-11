<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>


<c:choose>
	<c:when test="${ListaMovimentiForm.solleciti eq 'S'}">
		<span class="etichetta" style="font-weight: bolder;" >
				<bean:message key="servizi.erogazione.listaSolleciti" bundle="serviziLabels" />
		</span>
		<hr/>
		<table class="sintetica">
			<tr>
				<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
				</th>
				<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.progrSoll" bundle="serviziLabels" />
				</th>
				<th width="5%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.dataSoll" bundle="serviziLabels" />
				</th>
				<th  class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.noteSoll" bundle="serviziLabels" />
				</th>
				<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
				</th>
			</tr>
			<tr>
				<logic:iterate id="listaSoll" property="listaSollecitiUte" name="ListaMovimentiForm" indexId="idxSoll">
					<sbn:rowcolor var="color" index="idxSoll" />
					<tr bgcolor="${color}">
						<td class="testoNoBold" style="font-size: 90%; text-align:center;">
							<bean-struts:write name="listaSoll" property="codRichServ"  />
						</td>
						<td class="testoNoBold" style="font-size: 90%; text-align:center;">
							<bean-struts:write name="listaSoll" property="progrSollecito"  />
						</td>
						<td class="testoNoBold" style="font-size: 90%; text-align:center;">
							<bean-struts:write name="listaSoll" property="dataInvioString" />
						</td>
						<td class="testoNoBold" style="font-size: 90%;">
							<bean-struts:write name="listaSoll" property="note" />
						</td>
						<td class="testoNoBold" style="text-align:center;">
							<html:radio property="codSelMovSing" value="${listaSoll.codRichServ}" disabled="${ListaMovimentiForm.conferma}" />
						</td>
					</tr>
				</logic:iterate>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>
