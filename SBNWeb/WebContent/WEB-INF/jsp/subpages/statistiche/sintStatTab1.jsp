<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
	parameter="methodSinStatistiche" totBlocchi="totBlocchi" elementiPerBlocco="maxRighe"></sbn:blocchi>
<table class="sintetica">
	<tr>
		<th align="center">
		<div class="etichetta"><bean:message key="documentofisico.prg"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center"
			width="91%"><bean:message key="statistiche.lista.descrizione"
			bundle="statisticheLabels" /></th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center"
			width="2%"></th>
	</tr>
	<logic:notEmpty property="elencoStatistiche" name="sinteticaStatisticheForm">
		<logic:iterate id="item" property="elencoStatistiche" name="sinteticaStatisticheForm"
			indexId="riga">
			<sbn:rowcolor var="color" index="riga" />
			<tr class="testoNormale">
				<td bgcolor="${color}" width="3%" align="center">
				<div class="testoNormale"><bean-struts:write name="item" property="seqOrdinamento" /></div>
				</td>
				<c:choose>
					<c:when test="${item.tipoQuery eq '0'}">
						<td bgcolor="${color}">
						<div class="testoNormale"><bean-struts:write name="item"
							property="nomeStatistica" /></div>
						</td>
					</c:when>
					<c:otherwise>
						<td bgcolor="${color}"  style="text-align: left"><sbn:linkbutton
							name="item" property="selezRadio" index="riga" value="nomeStatistica"
							title="Sintetica Variabili Statistica" key="button.conferma"
							bundle="statisticheLabels" checkAttivita="st" /></td>
					</c:otherwise>
				</c:choose>
				<td  style="text-align: center" bgcolor="${color}"><html:radio
					property="selezRadio" value="${riga}"></html:radio></td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
<div id="divFooter">
<table border="0" style="height: 40px" align="center">
	<tr>
		<c:choose>
			<c:when test="${sinteticaStatisticheForm.disable == false}">
				<td width="100%"><html:submit styleClass="pulsanti"
					property="methodSinStatistiche">
					<bean:message key="button.conferma" bundle="statisticheLabels" />
				</html:submit> <html:submit styleClass="pulsanti" property="methodSinStatistiche">
					<bean:message key="button.indietro" bundle="statisticheLabels" />
				</html:submit></td>
			</c:when>
			<c:otherwise>
				<td width="100%"><html:submit styleClass="pulsanti"
					property="methodSinStatistiche">
					<bean:message key="button.indietro" bundle="statisticheLabels" />
				</html:submit></td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
</div>
