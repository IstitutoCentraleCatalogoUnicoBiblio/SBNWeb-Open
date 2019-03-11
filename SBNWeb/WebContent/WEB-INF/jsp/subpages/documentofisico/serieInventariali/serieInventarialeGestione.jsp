<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<div id="divForm">
<div id="divMessaggio"><sbn:errors bundle="documentoFisicoMessages" /></div>
<table width="100%">
	<tr>
		<td colspan="3">
		<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
			bundle="documentoFisicoLabels" /> <html:text disabled="true"
			styleId="testoNormale" property="codBib" size="3" maxlength="3"></html:text>
		<bean-struts:write name="serieInventarialeGestioneForm"
			property="descrBib" /></div>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td width="10%"><bean:message
			key="documentofisico.serieT" bundle="documentoFisicoLabels" /></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disableSerie}"
			styleId="testoNormale" property="recSerie.codSerie" size="3" maxlength="3"></html:text></td>
		<td><bean:message key="documentofisico.serieChiusaT"
			bundle="documentoFisicoLabels" /><html:checkbox property="flChiusa"
			disabled="${serieInventarialeGestioneForm.disable}"></html:checkbox> <html:hidden
			property="flChiusa" value="false" /></td>
		<!--<td><bean:message key="documentofisico.serieDefaultT"
			bundle="documentoFisicoLabels" /><html:checkbox property="flDefault"
			disabled="${serieInventarialeGestioneForm.disable}"></html:checkbox> <html:hidden
			property="flDefault" value="false" /></td>	-->
					<td align="right"><bean:message key="documentofisico.buonoCaricoT"
							bundle="documentoFisicoLabels" />
					</td>
					<td><html:text disabled="${serieInventarialeGestioneForm.disableBuonoCarico}"
			styleId="testoNormale" property="recSerie.buonoCarico" size="10" maxlength="9"></html:text> </td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.descrizioneT"
			bundle="documentoFisicoLabels" /></td>
		<td colspan="4"><html:textarea
			disabled="${serieInventarialeGestioneForm.disable}" styleId="testoNormale"
			property="recSerie.descrSerie" cols="80"></html:textarea></td>
	</tr>
</table>
<table width="100%"
	style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
	<tr>
		<td width="48%">
		<div class="etichetta"><bean:message key="documentofisico.progrAutomT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="2"><html:text
			disabled="${serieInventarialeGestioneForm.disable}" styleId="testoNormale"
			property="recSerie.progAssInv" size="10" maxlength="9"></html:text></td>
		<td>
	</tr>
</table>
<br>
<table width="100%"
	style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
	<tr>
		<th width="48%"></th>
		<th></th>
		<th>
		<div class="etichetta"><bean:message
			key="documentofisico.dataIngrConvenzT" bundle="documentoFisicoLabels" /></div>
		</th>
	</tr>
	<tr>
		<td width="48%">
		<div class="etichetta"><bean:message
			key="documentofisico.progrPregressT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.pregrAssInv" size="15"
			maxlength="9"></html:text></td>
		<!--
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.dataIngrConvenzT" bundle="documentoFisicoLabels" /></div>
		</td>
		-->
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.dataIngrPregr" size="15"
			maxlength="10"></html:text><bean:message key="documentofisico.formatoData"
			bundle="documentoFisicoLabels" /></td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.numPartenzaT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.numMan" size="15" maxlength="9"></html:text></td>
		<!--
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.dataIngrConvenzT" bundle="documentoFisicoLabels" /></div>
		</td>
		-->
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.dataIngrMan" size="15"
			maxlength="10"></html:text><bean:message key="documentofisico.formatoData"
			bundle="documentoFisicoLabels" /></td>
	</tr>
</table>
<br>
<table width="100%"
	style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
	<tr>
		<th width="38%" align="left"><bean:message key="documentofisico.riservManT"
			bundle="documentoFisicoLabels" /></th>
		<th align="left"><bean:message key="documentofisico.dalNumero"
			bundle="documentoFisicoLabels" /></th>
		<th align="left"><bean:message key="documentofisico.alNumero"
			bundle="documentoFisicoLabels" /></th>
		<th align="left"><bean:message key="documentofisico.dataIngrConvenzT"
			bundle="documentoFisicoLabels" /></th>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.primoIntervallo" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.inizioMan1" size="10" maxlength="9"></html:text></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.fineMan1" size="10" maxlength="9"></html:text>
		<c:choose>
			<c:when test="${serieInventarialeGestioneForm.intervalloSelez ne 0}">
				<html:submit styleClass="buttonTminus"
					property="${sbn:getUniqueButtonName(serieInventarialeGestioneForm.SUBMIT_CANCELLA_INTERVALLO, 1)}"
					title="Elimina intervallo">
					<bean:message key="documentofisico.bottone.cancIntervallo"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
		</c:choose></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.dataIngrRis1" size="15"
			maxlength="10"></html:text><bean:message key="documentofisico.formatoData"
			bundle="documentoFisicoLabels" /></td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.secondoIntervallo" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.inizioMan2" size="10"
			maxlength="9"></html:text></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.fineMan2" size="10" maxlength="9"></html:text>
		<c:choose>
			<c:when test="${serieInventarialeGestioneForm.intervalloSelez ne 0}">
				<html:submit styleClass="buttonTminus"
					property="${sbn:getUniqueButtonName(serieInventarialeGestioneForm.SUBMIT_CANCELLA_INTERVALLO, 2)}"
					title="Elimina intervallo">
					<bean:message key="documentofisico.bottone.cancIntervallo"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
		</c:choose></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.dataIngrRis2" size="15"
			maxlength="10"></html:text><bean:message key="documentofisico.formatoData"
			bundle="documentoFisicoLabels" /></td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.terzoIntervallo" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.inizioMan3" size="10"
			maxlength="9"></html:text></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.fineMan3" size="10" maxlength="9"></html:text>
		<c:choose>
			<c:when test="${serieInventarialeGestioneForm.intervalloSelez ne 0}">
				<html:submit styleClass="buttonTminus"
					property="${sbn:getUniqueButtonName(serieInventarialeGestioneForm.SUBMIT_CANCELLA_INTERVALLO, 3)}"
					title="Elimina intervallo">
					<bean:message key="documentofisico.bottone.cancIntervallo"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
		</c:choose></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.dataIngrRis3" size="15"
			maxlength="10"></html:text><bean:message key="documentofisico.formatoData"
			bundle="documentoFisicoLabels" /></td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.quartoIntervallo" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.inizioMan4" size="10"
			maxlength="9"></html:text></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.fineMan4" size="10" maxlength="9"></html:text>
		<c:choose>
			<c:when test="${serieInventarialeGestioneForm.intervalloSelez ne 0}">
				<html:submit styleClass="buttonTminus"
					property="${sbn:getUniqueButtonName(serieInventarialeGestioneForm.SUBMIT_CANCELLA_INTERVALLO, 4)}"
					title="Elimina intervallo">
					<bean:message key="documentofisico.bottone.cancIntervallo"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
		</c:choose></td>
		<td><html:text disabled="${serieInventarialeGestioneForm.disable}"
			styleId="testoNormale" property="recSerie.dataIngrRis4" size="15"
			maxlength="10"></html:text><bean:message key="documentofisico.formatoData"
			bundle="documentoFisicoLabels" /></td>
	</tr>
</table>
<c:choose>
	<c:when test="${serieInventarialeGestioneForm.date==true}">
		<table border="0" align="center">
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.dataInserimentoT"
					bundle="documentoFisicoLabels" /><bean-struts:write
					name="serieInventarialeGestioneForm" property="recSerie.dataIns" /></div>
				</td>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.dataAggiornamentoT"
					bundle="documentoFisicoLabels" /><bean-struts:write
					name="serieInventarialeGestioneForm" property="recSerie.dataAgg" /></div>
				</td>
			</tr>
		</table>
	</c:when>
</c:choose>
<br></div>
<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
<c:choose>
	<c:when test="${serieInventarialeGestioneForm.conferma}">
		<table align="center">
			<tr>
				<td><html:submit styleClass="pulsanti" property="methodSerieGest">
					<bean:message key="documentofisico.bottone.si"
						bundle="documentoFisicoLabels" />
				</html:submit> <html:submit styleClass="pulsanti"
					property="methodSerieGest">
					<bean:message key="documentofisico.bottone.no"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table align="center">
			<tr>
				<td><c:choose>
					<c:when test="${serieInventarialeGestioneForm.esamina}">
						<html:submit styleClass="pulsanti" property="methodSerieGest">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti" property="methodSerieGest">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodSerieGest">
							<bean:message key="documentofisico.bottone.salva"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
	</c:otherwise>
</c:choose></div>
