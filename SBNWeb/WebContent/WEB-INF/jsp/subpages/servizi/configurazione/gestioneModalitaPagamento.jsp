<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>

<c:choose>
<c:when test="${not empty navForm.modalitaPagamento}">
	<table class="sintetica">

		<tr class="etichetta" bgcolor="#dde8f0">
			<th width="6%" class="etichetta" scope="col" style="text-align:center;">
				<bean:message	key="servizi.utenti.titCodice" bundle="serviziLabels" />
			</th>
		    <th class="etichetta" scope="col" style="text-align:center;">
		    	<bean:message	key="servizi.label.descrizione" bundle="serviziLabels" />
		    </th>
			<th class="etichetta" scope="col"  style="width:4%; text-align:center;">
				<bean:message	key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
			</th>
		</tr>

		<logic:iterate	id       = "item"
						property = "modalitaPagamento"
						name     = "navForm"
						indexId  = "riga">
			<sbn:rowcolor var="color" index="riga" />
			<tr>
				<td bgcolor="${color}" align="center">
					<bean-struts:write	name="item" property="codModPagamento" />
				</td>
				<td bgcolor="${color}" class="testoNormale">
					<bean-struts:write	name="item" property="descrizione" />
				</td>
				<td bgcolor="${color}" align="center">
					<html:multibox property="modalitaSelezionate" value="${item.id}" titleKey="servizi.configurazione.controlloIter.selezioneMultipla" bundle="serviziLabels"></html:multibox>
					<html:hidden property="modalitaSelezionate" value="0" />
				</td>
			</tr>
		</logic:iterate>

	</table>
</c:when>
</c:choose>

<c:choose>
	<c:when test="${navForm.aggiungiModalita}">
		<br/>
		<br/>
		<bean:message key="servizi.configurazione.controlloIter.codiceModalita" bundle="serviziLabels" />&nbsp;&nbsp;
		<html:text styleId="testoNoBold" readonly="${navForm.conferma}" property="modalitaVO.codModPagamento" size="4" maxlength="4"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<bean:message key="servizi.label.descrizione" bundle="serviziLabels" />&nbsp;&nbsp;
		<html:text styleId="testoNoBold" readonly="${navForm.conferma}" property="modalitaVO.descrizione" size="40" maxlength="255"></html:text>
	</c:when>
</c:choose>

<br/>

