<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<div id="divMessaggio"><sbn:errors /></div>
	<sbn:navform action="/gestionestampe/conservazione/stampaRegistroConservazione.do"
		enctype="multipart/form-data">
		<div id="divForm">
		<table width="100%" align="center">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaRegistroConservazione">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaRegistroConservazioneForm" property="descrBib" /></td>
			</tr>
		</table>
		<table width="100%">
			<tr><td width="15%">&nbsp;</td></tr>
			<tr>
				<th class="etichetta">Filtri</th>
			</tr>
			<tr>
				<td><bean:message key="documentofisico.statoConservazioneT"
					bundle="documentoFisicoLabels" /></td>
				<td colspan="4"><html:select property="codiceStatoConservazione">
					<html:optionsCollection property="listaCodStatoConservazione" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td><bean:message key="documentofisico.tipoMaterialeT"
					bundle="documentoFisicoLabels" /></td>
				<td colspan="4"><html:select property="codiceTipoMateriale">
					<html:optionsCollection property="listaTipoMateriale" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td class="testoBold" align="left"><bean:message key="documentofisico.ordinamento"
					bundle="documentoFisicoLabels" /></td>
				<td class="testoNormale"><html:select property="tipoOrdinamento">
					<html:optionsCollection property="listaTipiOrdinamento" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		<BR>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th colspan="5" class="etichetta" width="50%"></th>
			</tr>
			<tr height="30">
				<c:choose>
					<c:when test="${stampaRegistroConservazioneForm.folder eq 'RangeInv'}">
						<td width="33%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaRegistroConservazione"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaRegistroConservazioneForm.folder eq 'Collocazioni'}">
						<td width="33%" class="schedaOn">
						<div align="center">Collocazione</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaRegistroConservazione"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerCollocazione"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaRegistroConservazioneForm.folder eq 'Inventari'}">
						<td width="33%" class="schedaOn">
						<div align="center">Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="33%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodStampaRegistroConservazione" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerInventari" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${stampaRegistroConservazioneForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${stampaRegistroConservazioneForm.folder eq 'Collocazioni'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
			</c:when>
			<c:when test="${stampaRegistroConservazioneForm.folder eq 'Inventari'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<HR>
		 <bean-struts:size id="comboSize" name="stampaRegistroConservazioneForm"
			property="elencoModelli" /> <!-- INIZIO TRATTAMENTO MODELLO -->
		<table width="100%" border="0">
			<tr>
				<logic:greaterEqual name="comboSize" value="2">
					<!--Selezione Modello Via Combo-->
					<td width="15%" scope="col">
					<div align="left" class="etichetta"><bean:message
						key="biblioteche.label.modello" bundle="gestioneStampeLabels" /></div>
					</td>
					<td colspan="5" valign="top" scope="col" align="left"><html:select
						styleClass="testoNormale" property="tipoModello" style="width:205px">
						<html:optionsCollection property="elencoModelli" value="jrxml" label="descrizione" />
					</html:select></td>
				</logic:greaterEqual>
				<logic:lessThan name="comboSize" value="2">
					<!--Selezione Modello Hidden-->
					<td width="15%" scope="col">
					<div align="left" class="etichetta">&nbsp;</div>
					</td>
					<td colspan="5" valign="top" scope="col" align="left">&nbsp; <html:hidden
						property="tipoModello"
						value="${stampaRegistroConservazioneForm.elencoModelli[0].jrxml}" /></td>
				</logic:lessThan>
			</tr>
		</table>
		<!-- FINE TRATTAMENTO MODELLO -->
		<HR>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
		</table>
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaRegistroConservazioneForm.disable == false}">
						<td><html:submit property="methodStampaRegistroConservazione">
							<bean:message key="documentofisico.bottone.conferma" bundle="documentoFisicoLabels" />
						</html:submit><html:submit property="methodStampaRegistroConservazione">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaRegistroConservazione">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
