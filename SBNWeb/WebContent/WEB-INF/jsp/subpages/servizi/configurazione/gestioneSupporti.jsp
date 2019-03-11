<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>

<table class="sintetica">
   	<tr class="etichetta" bgcolor="#dde8f0">

	    <th class="testoNormale" scope="col" style="width:7%; text-align:center;">
	    	<bean:message	key="servizi.utenti.titCodice"
							bundle="serviziLabels" />
	    </th>
	    <th class="etichetta" scope="col" style="text-align:center;">
	    	<bean:message	key="servizi.label.descrizione" bundle="serviziLabels" />
	    </th>
	    <th class="testoNormale" scope="col" style="width:9%; text-align:center;">
	    	<bean:message	key="servizi.erogazione.contesto"
	    					bundle="serviziLabels" />
	    </th>
	    <th class="testoNormale" scope="col" style="width:9%; text-align:center;">
	    	<bean:message	key="servizi.label.tariffaBase"
	    					bundle="serviziLabels" />
	    </th>
	    <th class="testoNormale" scope="col" style="width:9%; text-align:center;">
	    	<bean:message	key="servizi.label.costoUnitario"
	    					bundle="serviziLabels" />
	    </th>
	    <th scope="col" style="width:3%; text-align:center;">
		&nbsp;
		</th>
		<%--
		<th class="etichetta" scope="col"  style="width:4%; text-align:center;">
			<bean:message	key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
		</th>
		--%>
      	</tr>

		<c:choose>
			<c:when test="${not empty ConfigurazioneForm.supportiBiblioteca}">
			<logic:iterate	id       = "item"
	       					property = "supportiBiblioteca"
							name     = "ConfigurazioneForm"
							indexId  = "riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr>

					<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
						<sbn:linkbutton index="value.codSupporto"
						name="item" value="value.codSupporto" key="servizi.bottone.esamina"
						bundle="serviziLabels" title="esamina" property="codiceSupporto" /></td>
					<td bgcolor="${color}" class="testoNormale">
						<bean-struts:write	name="item" property="value.descrizione" />
					</td>
					<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
						<c:if test="${item.value.locale}"><bean:message key="servizi.erogazione.contesto.locale" bundle="serviziLabels" /></c:if>
						<c:if test="${not item.value.locale}"><bean:message key="servizi.erogazione.contesto.ill" bundle="serviziLabels" /></c:if>
					</td>
					<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
						<bean-struts:write	name="item" property="value.costoFisso" />
					</td>
					<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
						<bean-struts:write	name="item" property="value.importoUnitario" />
					</td>
					<td bgcolor="${color}" align="center">
						<html:radio property="codiceSupporto" value="${item.value.codSupporto}" titleKey="servizi.configurazione.supporti.selezioneSingola" bundle="serviziLabels"></html:radio>
					</td>
					<%--
					<td bgcolor="${color}" align="center">
						<html:multibox property="supportiSelezionati" titleKey="servizi.configurazione.controlloIter.selezioneMultipla" bundle="serviziLabels" value="${item.value.id}"></html:multibox>
					</td>
					--%>
		        </tr>
			</logic:iterate>
			</c:when>
		</c:choose>

</table>
<br/>