<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<div id="divMessaggio">
		<sbn:errors />
	</div>
	<sbn:navform
		action="/gestionestampe/strumentiPatrimonio/stampaStrumentiPatrimonio.do"
		enctype="multipart/form-data">
		<div id="divForm">
			<table width="100%" align="center">
				<tr>
					<td class="etichetta" width="10%"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />
					</td>
					<td><html:text disabled="true" styleId="testoNormale"
							property="codBib" size="5" maxlength="3"></html:text> <html:submit
							disabled="false" title="Lista Biblioteche"
							styleClass="buttonImageListaSezione"
							property="methodStampaStrumentiPatrimonio">
							<bean:message key="documentofisico.lsBib"
								bundle="documentoFisicoLabels" />
						</html:submit> <bean-struts:write name="stampaStrumentiPatrimonioForm"
							property="descrBib" />
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td width="15%">&nbsp;</td>
				</tr>
				<tr>
					<th class="etichetta">Filtri</th>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.statoConservazioneT"
							bundle="documentoFisicoLabels" />
					</td>
					<td colspan="4"><html:select
							property="codiceStatoConservazione">
							<html:optionsCollection property="listaCodStatoConservazione"
								value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.nonDisponibilePerT"
							bundle="documentoFisicoLabels" />
					</td>
					<%-- motivo noDisp --%>
					<td colspan="4"><html:select property="codNoDispo">
							<html:optionsCollection property="listaCodNoDispo" value="codice"
								label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.tipoMaterialeT"
							bundle="documentoFisicoLabels" />
					</td>
					<td colspan="4"><html:select property="codiceTipoMateriale">
							<html:optionsCollection property="listaTipoMateriale"
								value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
						<td><bean:message key="documentofisico.tipoDigitalizzazioneT" bundle="documentoFisicoLabels" /></td>
						<td colspan="3">
							<html:select property="richiesta.digitalizzazione">
								<html:option value="" />
								<html:option value="S" key="label.si" bundle="esportaLabels" />
								<html:option value="N" key="label.no" bundle="esportaLabels" />

							</html:select>

							<bean:message key="label.esporta.escludi.altre.copie.digit" bundle="esportaLabels" />
							<html:checkbox property="richiesta.escludiDigit" /><html:hidden property="richiesta.escludiDigit" value="false" />
						</td>
						<td>
							<html:select property="richiesta.tipoDigit">
								<html:option value="C" key="label.tipoDigit.completa" bundle="esportaLabels" />
								<html:option value="P" key="label.tipoDigit.parziale" bundle="esportaLabels" />
								<html:option value="T" key="label.tipoDigit.tutti" bundle="esportaLabels" />
							</html:select>
						</td>
					</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="testoBold" align="left"><bean:message
							key="documentofisico.ordinamento" bundle="documentoFisicoLabels" />
					</td>
					<td class="testoNormale"><html:select
							property="tipoOrdinamento">
							<html:optionsCollection property="listaTipiOrdinamento"
								value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table>
			<BR>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<th colspan="5" class="etichetta" width="50%"></th>
				</tr>
				<tr height="30">
					<c:choose>
						<c:when
							test="${stampaStrumentiPatrimonioForm.folder eq 'RangeInv'}">
							<td width="20%" class="schedaOn">
								<div align="center">Intervallo di Inventari</div></td>
						</c:when>
						<c:otherwise>
							<td width="20%" class="schedaOff">
								<div align="center">
									<html:submit property="methodStampaStrumentiPatrimonio"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerRangeInv"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when
							test="${stampaStrumentiPatrimonioForm.folder eq 'Collocazioni'}">
							<td width="20%" class="schedaOn">
								<div align="center">Collocazione</div></td>
						</c:when>
						<c:otherwise>
							<td width="20%" class="schedaOff">
								<div align="center">
									<html:submit property="methodStampaStrumentiPatrimonio"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerCollocazione"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when
							test="${stampaStrumentiPatrimonioForm.folder eq 'Inventari'}">
							<td width="20%" class="schedaOn">
								<div align="center">Inventari</div></td>
						</c:when>
						<c:otherwise>
							<td width="20%" class="schedaOff">
								<div align="center" styleId="etichetta">
									<html:submit property="methodStampaStrumentiPatrimonio"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerInventari"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stampaStrumentiPatrimonioForm.folder eq 'Data'}">
							<td width="20%" class="schedaOn">
								<div align="center">Data</div></td>
						</c:when>
						<c:otherwise>
							<td width="20%" class="schedaOff">
								<div align="center" styleId="etichetta">
									<html:submit property="methodStampaStrumentiPatrimonio"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selData"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stampaStrumentiPatrimonioForm.folder eq 'IdentificativiTitoli'}">
							<td width="20%" class="schedaOn">
								<div align="center">Titoli</div></td>
						</c:when>
						<c:otherwise>
							<td width="20%" class="schedaOff">
								<div align="center" styleId="etichetta">
									<html:submit property="methodStampaStrumentiPatrimonio"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerIdentificativiTitoli"
													bundle="documentoFisicoLabels" />
									</html:submit>
								</div></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			<c:choose>
				<c:when test="${stampaStrumentiPatrimonioForm.folder eq 'RangeInv'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
				</c:when>
				<c:when
					test="${stampaStrumentiPatrimonioForm.folder eq 'Collocazioni'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
				</c:when>
				<c:when
					test="${stampaStrumentiPatrimonioForm.folder eq 'Inventari'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
				</c:when>
				<c:when test="${stampaStrumentiPatrimonioForm.folder eq 'Data'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selData.jsp" />
				</c:when>
				<c:when test="${stampaStrumentiPatrimonioForm.folder eq 'IdentificativiTitoli'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionestampe/schede/stampaSchedeTitoli.jsp" />
				</c:when>

				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<HR>
			<table width="100%" border="0">
				<tr>
					<td colspan="2"><bean:message
							key="documentofisico.registriXls" bundle="documentoFisicoLabels" />
					<bean-struts:size id="comboSize"
							name="stampaStrumentiPatrimonioForm" property="elencoModelli" />
						<logic:greaterEqual name="comboSize" value="2">
							<html:select styleClass="testoNormale"
									property="tipoModello" style="width:205px">
									<html:optionsCollection property="elencoModelli" value="jrxml"
										label="descrizione" />
								</html:select>
						</logic:greaterEqual> <logic:lessThan name="comboSize" value="2">
							<!--Selezione Modello Hidden-->
							<td valign="top" scope="col" align="left">&nbsp;</td>
						</logic:lessThan> <html:hidden property="tipoModello"
							value="${stampaStrumentiPatrimonioForm.elencoModelli[0].jrxml}" />
					</td>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.moduloPrelievo"
							bundle="documentoFisicoLabels" />
					<html:checkbox disabled="${disableModPrel}"
							property="modPrel"></html:checkbox> <html:hidden
							property="modPrel" value="false" />
					</td>
					<td><table width="100%" border="0">
							<tr>
								<td><bean:message key="documentofisico.dataPrelievo"
										bundle="documentoFisicoLabels" /> <html:text
										styleId="testoNormale" property="dataPrelievo" size="10"
										maxlength="10"></html:text>
								</td>
								<td><bean:message key="documentofisico.motivoPrelievo"
										bundle="documentoFisicoLabels" /> <html:text
										styleId="testoNormale" property="motivoPrelievo" size="30"
										maxlength="30"></html:text>
								</td>
							<tr>
						</table>
					</td>
				</tr>
			</table>
			<HR>
		</div>
		<div id="divFooter">
			<!-- BOTTONIERA inserire solo i SOLO td -->
			<table align="center" border="0" style="height: 40px">
				<tr>
					<c:choose>
						<c:when test="${stampaStrumentiPatrimonioForm.disable == false}">
							<td><html:submit property="methodStampaStrumentiPatrimonio">
									<bean:message key="documentofisico.bottone.conferma"
										bundle="documentoFisicoLabels" />
								</html:submit> <html:submit property="methodStampaStrumentiPatrimonio">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</td>
						</c:when>
						<c:otherwise>
							<td><html:submit property="methodStampaStrumentiPatrimonio">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
