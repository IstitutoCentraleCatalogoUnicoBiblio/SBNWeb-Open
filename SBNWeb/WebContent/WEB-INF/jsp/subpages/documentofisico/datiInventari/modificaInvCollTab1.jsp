<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<table width="100%" border="0">
	<tr>
		<!-- prima riga sequenza + dataIngresso-->
		<td width="16%">
		<div class="testo"><bean:message key="documentofisico.sequenzaT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text styleId="testo" property="recInv.seqColl" size="25" maxlength="20"></html:text></td>
		<td>
		<div class="testo"><bean:message key="documentofisico.dataIngressoT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo" colspan="2"><html:text styleId="testo"
			disabled="${modificaInvCollForm.disableDataIns}" property="recInv.dataIngresso"
			size="15" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<!-- seconda riga tipoAcq + proven-->
		<td>
		<div class="testo"><bean:message key="documentofisico.tipoAcquisizioneT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><%--<html:select property="recInv.codTipoOrd"--%> <html:select
			property="recInv.tipoAcquisizione" disabled="${modificaInvCollForm.disableCodTipOrd}">
			<html:optionsCollection property="listaTipoOrdine" value="codice" label="descrizione" />
		</html:select></td>
		<td>
		<div class="testo"><bean:message key="documentofisico.provenienzaT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="2"><html:text styleId="testo" property="descrProven"
			disabled="${modificaInvCollForm.disableProven}" size="40" maxlength="255"></html:text>
		<html:submit title="Lista Provenienze" styleClass="buttonImageListaSezione"
			disabled="${modificaInvCollForm.disableTastoProven}" property="methodModInvColl">
			<bean:message key="documentofisico.lsProven" bundle="documentoFisicoLabels" />
		</html:submit></span></td>
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
		<td class="testo" colspan="2"><html:text styleId="testo" property="recInv.importo"
			size="20" maxlength="14"></html:text></td>
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
			<html:optionsCollection property="listaRiproducibilita" value="codice"
				label="descrizione" />
		</html:select></td>
	</tr>
	<tr>
		<!-- sesta riga tipoFrui + nonDisp-->
		<td>
		<div class="testo"><bean:message key="documentofisico.tipoFruizioneT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:select style="width:180px" property="recInv.codFrui">
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
			disabled="${modificaInvCollForm.disableDataIns}" property="recInv.dataRedisp" size="15"
			maxlength="10"></html:text></td>
	</tr>
	<!-- note -->
	<tr>
		<c:choose>
			<c:when
				test='${modificaInvCollForm.recInv.listaNote eq null}'>
				<tr>
					<td class="etichetta" width="16%"><bean:message key="documentofisico.note"
						bundle="documentoFisicoLabels" /></td>
					<td width="5%"><html:submit styleClass="buttonImageNewLine"
						property="methodModInvColl" title="Inserisci nota">
						<bean:message key="documentofisico.bottone.insNota" bundle="documentoFisicoLabels" />
					</html:submit></td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="etichetta" width="16%" valign="top"><bean:message
						key="documentofisico.note" bundle="documentoFisicoLabels" /></td>
					<td colspan="5">
					<table width="100%"
						style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
						<tr>
							<td bgcolor="#dde8f0"><bean:message key="documentofisico.codiceT"
								bundle="documentoFisicoLabels" /></td>
							<td bgcolor="#dde8f0"><bean:message key="documentofisico.descrizioneT"
								bundle="documentoFisicoLabels" /></td>
							<td bgcolor="#dde8f0"><html:submit styleClass="buttonImageNewLine"
								property="methodModInvColl" title="Inserisci nota">
								<bean:message key="documentofisico.bottone.insNota" bundle="documentoFisicoLabels" />
							</html:submit></td>
						</tr>
						<logic:iterate id="itemNota" property="listaNoteDinamica" name="modificaInvCollForm"
							indexId="idxNota">
							<c:set var="color">
								<c:choose>
									<c:when test='${color == "#FFCC99"}'>
				            #FEF1E2
				        </c:when>
									<c:otherwise>
							#FFCC99
				        </c:otherwise>
								</c:choose>
							</c:set>
							<tr>
								<td bgcolor="#dde8f0" valign="top"><html:select name="itemNota"
									property="codice1" indexed="true">
									<html:optionsCollection property="listaCodiciNote" value="codice"
										label="codiceDescrizione" />
								</html:select></td>
								<td bgcolor="#dde8f0" valign="top"><html:textarea name="itemNota" cols="80"
									rows="2" property="descrizione1" indexed="true"></html:textarea>&nbsp;&nbsp;&nbsp;<sbn:tastiera
									name="modificaInvCollForm"
									property='<%="listaNoteDinamica[" + idxNota + "].descrizione1"%>' /></td>
								<td bgcolor="#FFCC99"><html:submit styleClass="buttonImageDelLine"
									property="${sbn:getUniqueButtonName(modificaInvCollForm.SUBMIT_CANCELLA_NOTA, idxNota)}"
									title="Cancella nota">
									<bean:message key="documentofisico.bottone.cancNota"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</tr>
						</logic:iterate>
					</table>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tr>
	<!-- fine note -->
	<tr>
		<td>
		<div class="testo"><bean:message key="documentofisico.precisazioneVolumeT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="5"><html:textarea cols="80" rows="3" property="recInv.precInv"></html:textarea></td>
	</tr>
	<c:choose>
		<c:when test="${modificaInvCollForm.periodico}">
			<tr>
				<td>
				<div class="testo"><bean:message key="documentofisico.annoAbbT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:text styleId="testo" property="recInv.annoAbb" size="10" maxlength="4"></html:text></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.numVolT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td class="testo"><html:text styleId="testo" property="recInv.numVol" size="10"
					maxlength="9"></html:text></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.dataInserimentoT"
			bundle="documentoFisicoLabels" />
		</td>
		<td><bean-struts:write name="modificaInvCollForm" property="recInv.dataIns" />
		</div>
		</td>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.dataAggiornamentoT"
			bundle="documentoFisicoLabels" />
		</td>
		<td colspan="2"><bean-struts:write name="modificaInvCollForm"
			property="recInv.dataAgg" />
		</div>
		</td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message
			key="documentofisico.dataBollettinoNuoveAccessioniT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:text styleId="testo"
			disabled="${modificaInvCollForm.disableDataIns}" property="recInv.dataInsPrimaColl"
			size="15" maxlength="10"></html:text></td>
	</tr>
</table>
