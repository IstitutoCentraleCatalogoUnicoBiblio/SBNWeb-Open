<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="100%" border="0">
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="documentofisico.bidDaFile"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th colspan="4">
		<div class="testoNormale"><html:radio property="selezione" value="F"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
</table>

<table width="100%" border="0">
	<c:choose>
		<c:when test="${currentForm.nomeFileAppoggioBid ne null}">
			<tr>
				<td colspan="2"></td>
				<td colspan="5"><bean-struts:write name="currentForm" property="nomeFileAppoggioBid" /></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.nomeFileAppoggioT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="4"><html:file property="fileEsterno" name="currentForm" size="80" />
		<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone"
			bundle="documentoFisicoLabels" /> <html:submit title="Nome File Esterno"
			styleClass="pulsanti" property="${msg1}">
			<bean:message key="documentofisico.caricaFile" bundle="documentoFisicoLabels" />
		</html:submit></td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="documentofisico.bidT"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th colspan="4">
		<div class="testoNormale"><html:radio property="selezione" value="N"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
</table>

<!-- almaviva2 01.02.2010 parte nuova per filtro per titoli -->
<table width="100%" border="0">
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="cataloghi.label.filtriTitoli"
			bundle="gestioneStampeLabels" /></div>
		</th>
		<th colspan="4">
		<div class="testoNormale"><html:radio property="selezione" value="T"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td>
			<div class="etichetta"><bean:message key="cataloghi.label.natura" bundle="gestioneStampeLabels" /></div>
		</td>
		<td><html:select styleClass="testoNormale" property="natura">
			<html:optionsCollection property="listaNatura" value="codice" label="descrizione" />
			</html:select>
		</td>
		<td>
			<div class="etichetta"><bean:message key="cataloghi.label.genere" bundle="gestioneStampeLabels" /></div>
		</td>
		<td><html:select styleClass="testoNormale" property="descGenere">
			<html:optionsCollection property="listaGenere" value="codice" label="descrizione" />
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<div class="etichetta"><bean:message key="ricerca.label.paese" bundle="gestioneStampeLabels" /></div>
		</td>
		<td><html:select styleClass="testoNormale" property="descPaese">
			<html:optionsCollection property="listaPaese" value="codice" label="descrizione" />
			</html:select>
		</td>
		<td>
			<div class="etichetta"><bean:message key="cataloghi.label.lingua" bundle="gestioneStampeLabels" /></div>
		</td>
		<td><html:select styleClass="testoNormale" property="descLingua">
			<html:optionsCollection property="listaLingua" value="codice" label="descrizione" />
			</html:select>
		</td>
	</tr>
	<tr>
	<td>
		<div class="etichetta"><bean:message key="cataloghi.label.tipoData" bundle="gestioneStampeLabels" /></div>
		</td>
		<td><html:select styleClass="testoNormale" property="descTipoData">
			<html:optionsCollection property="listaTipoData" value="codice" label="descrizione" />
			</html:select>
		</td>
		<td>
			<div class="etichetta"><bean:message key="cataloghi.label.status" bundle="gestioneStampeLabels" /></div>
		</td>
		<td><html:select styleClass="testoNormale" property="descLivAut">
			<html:optionsCollection property="listaLivAut" value="codice" label="descrizione" />
			</html:select>
		</td>
	</tr>

</table>

