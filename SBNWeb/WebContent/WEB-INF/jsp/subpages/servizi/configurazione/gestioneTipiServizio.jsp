<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<div>
	<div style="float:none;">
		<table width="100%" border="0" class="sintetica">
			<tr bgcolor="#dde8f0">
				<!-- <th class="etichetta" scope="col"></th> -->
				<th class="etichetta" scope="col" style="width:7%; text-align:center;"><bean:message key="servizi.utenti.titCodice"
					bundle="serviziLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="servizi.autorizzazioni.header.desSer"
					bundle="serviziLabels" /></th>
				<th scope="col"></th>
			</tr>
			<logic:notEmpty property="lstTipiServizio" name="ConfigurazioneForm">
				<logic:iterate id="item" property="lstTipiServizio" name="ConfigurazioneForm"
					indexId="listaIdx">
					<c:set var="riga">${listaIdx + 1}</c:set>
					<sbn:rowcolor var="color" index="listaIdx" />
					<tr bgcolor="#FFCC99">

						<!-- <td bgcolor="${color}" class="testoNormale"><bean-struts:write name="riga" /></td> -->

						<!-- <td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="codiceTipoServizio" /></td> -->

						<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
						<sbn:linkbutton index="listaIdx"
						name="item" value="codiceTipoServizio" key="servizi.bottone.esamina"
						bundle="serviziLabels" title="esamina" property="selectedTipoServizio" /></td>

						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrizione" /></td>
						<td bgcolor="${color}" class="testoNormale" style="text-align:center;"><html:radio property="selectedTipoServizio"
						value="${listaIdx}" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
</div>
</div>

<c:choose>
    <c:when test="${ConfigurazioneForm.stringaMessaggioServizioModalitaUltMod ne ''}">
		<br />
		<div class="msgWarning1"><bean-struts:write name="ConfigurazioneForm" property="stringaMessaggioServizioModalitaUltMod" />
		</div>
	</c:when>
</c:choose>
<c:choose>
    <c:when test="${ConfigurazioneForm.stringaMessaggioServizioSupportiUltSupp ne ''}">
		<br />
		<div class="msgWarning1"><bean-struts:write name="ConfigurazioneForm" property="stringaMessaggioServizioSupportiUltSupp" />
		</div>
	</c:when>
</c:choose>