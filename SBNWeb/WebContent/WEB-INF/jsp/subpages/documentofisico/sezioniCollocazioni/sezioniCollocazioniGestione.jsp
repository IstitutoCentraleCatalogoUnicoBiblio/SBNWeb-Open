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
		<bean-struts:write name="sezioniCollocazioniGestioneForm"
			property="descrBib" /></div>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.sezioneT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td>
		<div><html:text styleId="testoNormale"
			disabled="${sezioniCollocazioniGestioneForm.disableSez}"
			property="recSezione.codSezione" size="20" maxlength="10"></html:text></div>
		</td>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.inventariCollocT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text styleId="testoNormale"
			disabled="${sezioniCollocazioniGestioneForm.disableInvColl}"
			property="recSezione.inventariCollocati" size="10" maxlength="9"></html:text>
		</td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.tipoSezioneT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td>
		<div class="testoNormale"><html:select
			property="recSezione.tipoSezione" onchange="this.form.submit()"
			disabled="${sezioniCollocazioniGestioneForm.disableTipoSez}">
			<html:optionsCollection property="listaTipoSezione" value="codice"
				label="descrizioneCodice" />
		</html:select></div>
		</td>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.inventariPrevistiT"
			bundle="documentoFisicoLabels" /></div>
		</td>

		<td><html:text styleId="testoNormale"
			disabled="${sezioniCollocazioniGestioneForm.disableInvPrev}"
			property="recSezione.inventariPrevisti" size="10" maxlength="9"></html:text>
		</td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.tipoCollocazioneT" bundle="documentoFisicoLabels" /></div>
		</td>

		<td>
		<div class="testoNormale"><html:select
			property="recSezione.tipoColloc" onchange="this.form.submit()"
			disabled="${sezioniCollocazioniGestioneForm.disableTipoColl}">
			<html:optionsCollection property="listaTipoCollocazione"
				value="codice" label="descrizioneCodice" />
		</html:select></div>
		</td>

		<td><c:choose>
			<c:when test="${sezioniCollocazioniGestioneForm.tastoAggiorna}">
				<noscript><html:submit styleClass="pulsanti" disabled="false"
					property="methodSezCollGest">
					<bean:message key="documentofisico.aggiorna"
						bundle="documentoFisicoLabels" />
				</html:submit></noscript>
			</c:when>
		</c:choose></td>

		<td></td>
	</tr>
	<c:choose>
		<c:when test="${sezioniCollocazioniGestioneForm.ultPrgAss==true}">
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.ultProgrAssegnatoT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:text
					disabled="${sezioniCollocazioniGestioneForm.disableUltPrg}"
					styleId="testoNormale" property="recSezione.progNum" size="10"
					maxlength="9"></html:text></td>
			</tr>
		</c:when>
		<c:when test="${sezioniCollocazioniGestioneForm.sistCla==true}">
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.sistemaDiClassificazioneT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td><html:select
			property="recSezione.classific" onchange="this.form.submit()"
			disabled="${sezioniCollocazioniGestioneForm.disableSistClass}">
			<html:optionsCollection property="listaClassificazioni" value="codice"
				label="descrizioneCodice" />
		</html:select><%--<html:text
					disabled="${sezioniCollocazioniGestioneForm.disableSistClass}"
					styleId="testoNormale" property="recSezione.classific" size="10"
					maxlength="3"></html:text> --%></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<td>
		<div class="etichetta"><bean:message
			key="documentofisico.descrizioneT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:text
			disabled="${sezioniCollocazioniGestioneForm.disableDescr}"
			property="recSezione.descrSezione" size="40" maxlength="30"></html:text>
		</td>
		<td>
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.notaT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td>
		<div class="testoNormale"><html:textarea
			disabled="${sezioniCollocazioniGestioneForm.disableNote}"
			property="recSezione.noteSezione" cols="51" rows="5">
		</html:textarea></div>
		</td>
		<td>
		</td>
		<td>
		</td>
	</tr>
</table>
<c:choose>
	<c:when test="${sezioniCollocazioniGestioneForm.date==true}">
		<table align="center">
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
					name="sezioniCollocazioniGestioneForm"
					property="recSezione.dataIns" /></div>
				</td>
				<td>
				<div class="testoNormale"><bean-struts:write
					name="sezioniCollocazioniGestioneForm"
					property="recSezione.dataAgg" /></div>
				</td>
			</tr>
		</table>
	</c:when>
</c:choose></div>

<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
<c:choose>
	<c:when test="${sezioniCollocazioniGestioneForm.conferma}">
		<table align="center">
			<tr>
				<td><html:submit styleClass="pulsanti" property="methodSezCollGest">
					<bean:message key="documentofisico.bottone.si"
						bundle="documentoFisicoLabels" />
				</html:submit> <html:submit styleClass="pulsanti"
					property="methodSezCollGest">
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
					<c:when test="${sezioniCollocazioniGestioneForm.tastoFormati}">
						<html:submit styleClass="pulsanti" property="methodSezCollGest">
							<bean:message key="documentofisico.bottone.formati"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> <c:choose>
					<c:when test="${sezioniCollocazioniGestioneForm.esamina}">
						<html:submit styleClass="pulsanti" property="methodSezCollGest">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti"
							disabled="sezioniCollocazioniGestioneForm.disable"
							property="methodSezCollGest">
							<bean:message key="documentofisico.bottone.salva"
								bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodSezCollGest">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
	</c:otherwise>
</c:choose></div>
