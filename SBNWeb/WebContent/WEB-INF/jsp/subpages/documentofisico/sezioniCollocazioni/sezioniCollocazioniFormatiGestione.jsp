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
<table width="80%">
	<tr>
		<td class="etichetta">
		<bean:message key="documentofisico.bibliotecaT"
			bundle="documentoFisicoLabels" /> <html:text disabled="true"
			styleId="testoNormale" property="codBib" size="5" maxlength="3"></html:text>
		<bean-struts:write name="sezioniCollocazioniFormatiGestioneForm"
			property="descrBib" />
		</td>
	</tr>
</table>
<table width="80%">
	<tr>
		<td>
		<div align="left" class="etichetta"><bean:message key="documentofisico.sezioneT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="4"><html:text disabled="true" styleId="testoNormale"
			property="codSez" size="20" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.formatoT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<c:choose>
			<c:when test="${sezioniCollocazioniFormatiGestioneForm.prov eq 'modifica'}">
				<td><html:text
					disabled="${sezioniCollocazioniFormatiGestioneForm.disableFormato}"
					styleId="testoNormale" property="recFormatiSezioni.codFormato" style="width:20px"
					maxlength="2"></html:text> <html:text
					disabled="${sezioniCollocazioniFormatiGestioneForm.disableDescrFormato}"
					styleId="testoNormale" property="recFormatiSezioni.descrFor" size="35" maxlength="20"></html:text></td>
						<td><bean:message key="documentofisico.dimensioneDaT"
							bundle="documentoFisicoLabels" /> <html:text
							disabled="${sezioniCollocazioniFormatiGestioneForm.disableDimDa}" styleId="testoNormale"
							property="recFormatiSezioni.dimensioneDa" size="10" maxlength="4"></html:text> <bean:message
							key="documentofisico.dimensioneAT" bundle="documentoFisicoLabels" /> <html:text
							disabled="${sezioniCollocazioniFormatiGestioneForm.disableDimA}" styleId="testoNormale"
							property="recFormatiSezioni.dimensioneA" size="10" maxlength="4"></html:text></td>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${sezioniCollocazioniFormatiGestioneForm.noFormati}">
						<td colspan="4"><html:text disabled="true" styleId="testoNormale"
							property="recFormatiSezioni.codFormato" style="width:20px" maxlength="2"></html:text>
						</td>
					</c:when>
					<c:otherwise>
						<td><html:text
							disabled="${sezioniCollocazioniFormatiGestioneForm.disableFormato}"
							styleId="testoNormale" property="recFormatiSezioni.codFormato" style="width:20px"
							maxlength="2"></html:text> <html:text
							disabled="${sezioniCollocazioniFormatiGestioneForm.disableDescrFormato}"
							styleId="testoNormale" property="recFormatiSezioni.descrFor" size="35"
							maxlength="20"></html:text></td>
						<td><bean:message key="documentofisico.dimensioneDaT"
							bundle="documentoFisicoLabels" /> <html:text
							disabled="${sezioniCollocazioniFormatiGestioneForm.disableDimDa}" styleId="testoNormale"
							property="recFormatiSezioni.dimensioneDa" size="10" maxlength="4"></html:text> <bean:message
							key="documentofisico.dimensioneAT" bundle="documentoFisicoLabels" /> <html:text
							disabled="${sezioniCollocazioniFormatiGestioneForm.disableDimA}" styleId="testoNormale"
							property="recFormatiSezioni.dimensioneA" size="10" maxlength="4"></html:text></td>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		<noscript><html:submit styleClass="pulsanti" disabled="false"
			property="methodSezCollForGest">
			<bean:message key="documentofisico.aggiorna" bundle="documentoFisicoLabels" />
		</html:submit></noscript>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message key="documentofisico.nPezziT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="4"><html:text
			disabled="${sezioniCollocazioniFormatiGestioneForm.disableNPezzi}"
			styleId="testoNormale" property="recFormatiSezioni.numeroPezzi" size="10" maxlength="6"></html:text></td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message key="documentofisico.nSerieT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${sezioniCollocazioniFormatiGestioneForm.disableNSerie}"
			styleId="testoNormale" property="recFormatiSezioni.progSerie" size="10" maxlength="9"></html:text>
		</td>
		<td colspan="3"><bean:message key="documentofisico.nAssegnatoT"
			bundle="documentoFisicoLabels" /><html:text
			disabled="${sezioniCollocazioniFormatiGestioneForm.disablePrgAss}"
			styleId="testoNormale" property="recFormatiSezioni.progNum" size="10" maxlength="6"></html:text></td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message key="documentofisico.nPezziMiscT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="4"><html:text
			disabled="${sezioniCollocazioniFormatiGestioneForm.disableNPezziMisc}"
			styleId="testoNormale" property="recFormatiSezioni.numeroPezziMisc" size="10" maxlength="2"></html:text></td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message key="documentofisico.dalProgrMiscT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text disabled="${sezioniCollocazioniFormatiGestioneForm.disableNSerieNum1Misc}"
			styleId="testoNormale" property="recFormatiSezioni.progSerieNum1Misc" size="10" maxlength="9"></html:text>
		<html:text disabled="${sezioniCollocazioniFormatiGestioneForm.disableDalProgrMisc}"
			styleId="testoNormale" property="recFormatiSezioni.dalProgrMisc" size="10" maxlength="9"></html:text>
		</td>
		<td colspan="3"><bean:message key="documentofisico.alProgrMiscT"
			bundle="documentoFisicoLabels" />
			<html:text disabled="${sezioniCollocazioniFormatiGestioneForm.disableNSerieNum2Misc}"
			styleId="testoNormale" property="recFormatiSezioni.progSerieNum2Misc" size="10" maxlength="9"></html:text><html:text
			disabled="${sezioniCollocazioniFormatiGestioneForm.disableAlProgrMisc}"
			styleId="testoNormale" property="recFormatiSezioni.alProgrMisc" size="10" maxlength="6"></html:text></td>
	</tr>
</table>
<c:choose>
	<c:when test="${sezioniCollocazioniFormatiGestioneForm.date==true}">
		<table border="0" align="center">
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.dataInserimentoT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.dataAggiornamentoT"
					bundle="documentoFisicoLabels" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="testoNormale"><bean-struts:write
					name="sezioniCollocazioniFormatiGestioneForm"
					property="recFormatiSezioni.dataIns" /></div>
				</td>
				<td>
				<div class="testoNormale"><bean-struts:write
					name="sezioniCollocazioniFormatiGestioneForm"
					property="recFormatiSezioni.dataAgg" /></div>
				</td>
			</tr>
		</table>
	</c:when>
</c:choose></div>
<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
<c:choose>
	<c:when test="${sezioniCollocazioniFormatiGestioneForm.conferma}">
		<table align="center">
			<tr>
				<td><html:submit styleClass="pulsanti"
					property="methodSezCollForGest">
					<bean:message key="documentofisico.bottone.si"
						bundle="documentoFisicoLabels" />
				</html:submit> <html:submit styleClass="pulsanti" property="methodSezCollForGest">
					<bean:message key="documentofisico.bottone.no"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table align="center">
			<tr><td>
				<c:choose>
					<c:when
						test="${sezioniCollocazioniFormatiGestioneForm.prov eq 'nuova'}">
						<td><html:submit property="methodSezCollForGest">
							<bean:message key="documentofisico.lsFormatiSezioni"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
				</c:choose>
					<sbn:checkAttivita idControllo="df">
				<c:choose>
						<c:when
							test="${sezioniCollocazioniFormatiGestioneForm.prov ne 'esamina'}">
							<html:submit styleClass="pulsanti"
								property="methodSezCollForGest">
								<bean:message key="documentofisico.bottone.salva"
									bundle="documentoFisicoLabels" />
							</html:submit>
						</c:when>
				</c:choose>
				</sbn:checkAttivita>
				<html:submit styleClass="pulsanti" property="methodSezCollForGest">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
				</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose></div>
