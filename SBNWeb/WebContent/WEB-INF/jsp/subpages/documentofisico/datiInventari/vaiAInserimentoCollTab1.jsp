<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<c:choose>
	<c:when test="${vaiAInserimentoCollForm.tasto eq 'sceltaTitolo'}">
		<table width="100%" align="center">
			<tr>
				<td><bean:message key="documentofisico.titoloColl" bundle="documentoFisicoLabels" />:
				<span class="etichetta"></span> <bean-struts:write name="vaiAInserimentoCollForm"
					property="bidDaTit" /></td>
				<td><bean-struts:write name="vaiAInserimentoCollForm" property="isbdDaTit" /></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
			<br>
		<table width="100%" border="0">
		<c:choose>
				<c:when test="${vaiAInserimentoCollForm.prov eq 'CV'}">
					<tr>
						<td width="20%"><bean:message key="documentofisico.livelloDiCollocazioneT"
							bundle="documentoFisicoLabels" /></td>
						<td><bean:message
							key="documentofisico.alTitPart" bundle="documentoFisicoLabels" /><html:radio
							property="livColl" value="alTitPart" /></td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td width="20%"><bean:message
							key="documentofisico.livelloDiCollocazioneT" bundle="documentoFisicoLabels" /></td>
						<td><bean:message
							key="documentofisico.alTitGen" bundle="documentoFisicoLabels" /><html:radio
							property="livColl" value="alTitGen" onchange="this.form.submit()"  disabled="${vaiAInserimentoCollForm.disablePerModInvDaNav}"/></td>
						<td><bean:message key="documentofisico.alTitPart"
							bundle="documentoFisicoLabels" /><html:radio property="livColl" value="alTitPart"
							onchange="this.form.submit()" disabled="${vaiAInserimentoCollForm.disablePerModInvDaNav}"/></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</table>
	</c:otherwise>
</c:choose>
<jsp:include
	page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/datiCollocazione.jsp" />
<c:choose>
	<c:when test="${vaiAInserimentoCollForm.prov eq 'CV'}">
		<table width="100%" border="0">
			<tr>
				<td width="20%">
				<div class="etichetta"><bean:message key="documentofisico.stampaEtichettaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2"><html:checkbox property="stampaEtich"></html:checkbox><html:hidden
					property="stampaEtich" value="false" /></td>
			</tr>
		</table>
		<table width="100%" border="0" bgcolor="#FFCC99">
			<tr>
				<!-- prima riga sequenza + dataIngresso-->
				<td width="20%">
				<div class="testo"><bean:message key="documentofisico.sequenzaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:text styleId="testo" property="recInv.seqColl" size="25" maxlength="20"></html:text></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.dataIngressoT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td class="testo" colspan="2"><html:text styleId="testo"
					disabled="${vaiAInserimentoCollForm.disableDataIns}" property="recInv.dataIngresso"
					size="15" maxlength="10"></html:text></td>
			</tr>
			<tr>
				<!-- seconda riga tipoAcq + proven-->
				<td>
				<div class="testo"><bean:message key="documentofisico.tipoAcquisizioneT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td class="testo"><html:select property="recInv.tipoAcquisizione"
					disabled="${vaiAInserimentoCollForm.disableCodTipOrd}">
					<html:optionsCollection property="listaTipoOrdine" value="codice" label="descrizione" />
				</html:select></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.provenienzaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2"><html:text styleId="testo" property="descrProven" size="40"
					maxlength="45"></html:text> <html:submit title="Lista Provenienze"
					styleClass="buttonImageListaSezione" disabled="false" property="methodVaiAInsColl">
					<bean:message key="documentofisico.lsProven" bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
			<tr>
				<!-- terza riga  valore + prezzo-->
				<td>
				<div class="testo"><bean:message key="documentofisico.valoreInventarialeT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td class="testo"><html:text styleId="testo" property="recInv.valore" size="20"
					maxlength="14"></html:text></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.prezzoRealeT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td class="testo" colspan="2"><html:text styleId="testo"
					property="recInv.importo" size="20" maxlength="14"></html:text></td>
			</tr>
			<tr>
				<!-- quarta riga tipoMat + statoCons -->
				<td>
				<div class="testo"><bean:message key="documentofisico.tipoMaterialeT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:select property="recInv.codMatInv">
					<html:optionsCollection property="listaMatCons" value="codice" label="descrizione" />
				</html:select></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.tipoStatoConT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2"><html:select property="recInv.statoConser">
					<html:optionsCollection property="listaStatoCons" value="codice" label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<!-- quinta riga supportoCopia + riproducibilità-->
				<td>
				<div class="testo"><bean:message key="documentofisico.supportoCopiaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:select property="recInv.supportoCopia">
					<html:optionsCollection property="listaSupportoCopia" value="codice"
						label="descrizione" />
				</html:select></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.riproducibilitaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2"><html:select property="recInv.codRiproducibilita">
					<html:optionsCollection property="listaRiproducibilita" value="codice" label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<!-- sesta riga tipoFrui + nonDisp-->
				<td>
				<div class="testo"><bean:message key="documentofisico.tipoFruizioneT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:select property="recInv.codFrui">
					<html:optionsCollection property="listaTipoFruizione" value="codice"
						label="descrizione" />
				</html:select></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.nonDisponibilePerT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2"><html:select property="recInv.codNoDisp">
					<html:optionsCollection property="listaCodNoDispo" value="codice" label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<!-- settima data redisp-->
				<td colspan="2"></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.finoAlT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2" class="testo"><html:text styleId="testo"
					disabled="${vaiAInserimentoCollForm.disable}" property="recInv.dataRedisp" size="15"
					maxlength="10"></html:text></td>
			</tr>
			<tr>
				<td>
				<div class="testo"><bean:message key="documentofisico.precisazioneVolumeT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="5"><html:textarea cols="80" rows="3" property="recInv.precInv"></html:textarea></td>
			</tr>
			<c:choose>
				<c:when test="${vaiAInserimentoCollForm.periodico}">
					<tr>
						<td>
						<div class="testo"><bean:message key="documentofisico.annoAbbT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td><html:text styleId="testo" property="recInv.annoAbb" size="10"
							maxlength="9"></html:text></td>
						<td>
						<div class="testo"><bean:message key="documentofisico.numVolT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td class="testo"><html:text styleId="testo" property="recInv.numVol" size="10"
							maxlength="9"></html:text></td>
					</tr>
				</c:when>
			</c:choose>
		</table>
		<table width="100%" border="0">
			<tr>
				<td>
				<div class="testo"><bean:message
					key="documentofisico.dataBollettinoNuoveAccessioniT" bundle="documentoFisicoLabels" /></div>
				</td>
				<td class="testo"><html:text styleId="testo"
					disabled="${vaiAInserimentoCollForm.disable}" property="recInv.dataInsPrimaColl"
					size="15" maxlength="10"></html:text></td>
			</tr>
			<tr>
				<td width="20%"><bean:message key="documentofisico.dataInserimentoT"
					bundle="documentoFisicoLabels" /></td>
				<td><bean-struts:write name="vaiAInserimentoCollForm" property="recInv.dataIns" /></td>
				<td><bean:message key="documentofisico.dataAggiornamentoT"
					bundle="documentoFisicoLabels" /></td>
				<td colspan="2"><bean-struts:write name="vaiAInserimentoCollForm"
					property="recInv.dataAgg" /></td>
			</tr>
		</table>
	</c:when>
</c:choose>
