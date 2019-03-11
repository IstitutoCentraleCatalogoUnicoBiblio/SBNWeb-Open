<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<layout:page>
	<sbn:navform
		action="/documentofisico/elaborazioniDifferite/aggiornamentoDisponibilita.do"
		enctype="multipart/form-data">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<!--  biblioteca -->
			<table width="100%" border="0">
				<tr>
					<td class="etichetta" width="10%"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />
					</td>
					<td><html:text disabled="true" styleId="testoNormale"
							property="codBib" size="5" maxlength="3"></html:text> <html:submit
							disabled="false" title="Lista Biblioteche"
							styleClass="buttonImageListaSezione" property="methodAggDisp">
							<bean:message key="documentofisico.lsBib"
								bundle="documentoFisicoLabels" />
						</html:submit> <bean-struts:write name="aggiornamentoDisponibilitaForm"
							property="descrBib" /></td>
				</tr>
			</table>
			<table width="100%" border="0">
				<tr>
					<th class="etichetta" colspan="5" width="1%">Selezionare nuovi
						valori da assegnare</th>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td width="15%"><bean:message
							key="documentofisico.tipoFruizioneT"
							bundle="documentoFisicoLabels" />
					</td>
					<td><html:select property="codTipoFruizione">
							<html:optionsCollection property="listaCodTipoFruizione"
								value="codice" label="descrizione" />
						</html:select>
					</td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.nonDisponibilePerT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="codNoDispo">
							<html:optionsCollection property="listaCodNoDispo" value="codice"
								label="descrizione" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.riproducibilitaT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="codRip">
							<html:optionsCollection property="listaCodRiproducibilita"
								value="codice" label="descrizione" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td><bean:message key="documentofisico.statoConservazioneT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="codiceStatoConservazione">
							<html:optionsCollection property="listaStatoCons" value="codice"
								label="descrizione" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td width="20%"><bean:message
							key="documentofisico.tipoDigitalizzazioneT"
							bundle="documentoFisicoLabels" /></td>
					<td><html:select property="codDigitalizzazione">
							<html:optionsCollection property="listaTipoDigit" value="codice"
								label="descrizione" />
						</html:select></td>
					<td colspan="3"></td>
				</tr>
			</table>
			<table width="100%" style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
				<tr></tr>
				<tr>
					<th class="etichetta" width="15%">Filtri sul titolo:</th>
					<td width="18%"><bean:message key="documentofisico.livAutT"	bundle="documentoFisicoLabels" /></td>
					<td width="30%"><html:select
							property="filtroLivAut">
							<html:optionsCollection property="listaLivAut" value="codice"
								label="descrizione" />
						</html:select>
					</td>
					<td width="16%"><bean:message key="documentofisico.naturaT" bundle="documentoFisicoLabels" /></td>
					<td><html:select property="filtroNatura">
							<html:optionsCollection property="listaNatura" value="codice"
								label="descrizione" />
						</html:select></td>
				</tr>

				<tr>
					<th></th>
					<td width="18%"><bean:message key="label.tipodata" bundle="esportaLabels" /></td>
					<td width="30%"><html:select property="tipoData" style="width:200px">
										<html:optionsCollection property="listaTipoData" value="codice"
											label="descrizioneCodice" />
									</html:select></td>
					<td width="16%"><bean:message key="label.data1" bundle="esportaLabels" />&nbsp;<bean:message
										key="label.dal" bundle="esportaLabels" />&nbsp;<html:text property="aaPubbFrom" size="5"
										maxlength="4" /></td>
					<td><bean:message key="label.al" bundle="esportaLabels" />&nbsp;<html:text property="aaPubbTo" size="5"	maxlength="4" /></td>

				</tr>

				<tr></tr>
			</table>
			<table width="100%" style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
				<tr></tr>
				<tr>
					<th class="etichetta" width="15%">Filtri sull'inventario:</th>
					<td width="18%">con data d'ingresso <bean:message
							key="documentofisico.dataIngressoDaT"
							bundle="documentoFisicoLabels" />
					</td>
					<td colspan="4"><html:text styleId="testoNormale"
							property="filtroDataIngressoDa" size="10" maxlength="10"></html:text>
						<bean:message key="documentofisico.dataIngressoAT"
							bundle="documentoFisicoLabels" /> <html:text
							styleId="testoNormale" property="filtroDataIngressoA" size="10"
							maxlength="10"></html:text>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><bean:message key="documentofisico.tipoFruizioneT"
							bundle="documentoFisicoLabels" />
					</td>
					<td colspan="2"><html:select property="filtroTipoFruizione">
							<html:optionsCollection property="listaCodTipoFruizione"
								value="codice" label="descrizione" />
						</html:select>
					</td>
					<td><bean:message key="documentofisico.nonDisponibilePerT"
							bundle="documentoFisicoLabels" />
					</td>
					<td><html:select property="filtroNoDispo">
							<html:optionsCollection property="listaCodNoDispo" value="codice"
								label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><bean:message key="documentofisico.riproducibilitaT"
							bundle="documentoFisicoLabels" /></td>
					<td colspan="2"><html:select property="filtroRip">
							<html:optionsCollection property="listaCodRiproducibilita"
								value="codice" label="descrizione" />
						</html:select></td>
					<td><bean:message key="documentofisico.statoConservazioneT"
							bundle="documentoFisicoLabels" />
					</td>
					<td><html:select
							property="filtroStatoConservazione">
							<html:optionsCollection property="listaStatoCons" value="codice"
								label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
				<tr></tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr height="30">
					<c:choose>
						<c:when
							test="${aggiornamentoDisponibilitaForm.folder eq 'RangeInv'}">
							<td width="33%" class="schedaOn">
								<div align="center">Intervallo di Inventari</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="33%" class="schedaOff">
								<div align="center">
									<html:submit property="methodAggDisp"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerRangeInv"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div>
							</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when
							test="${aggiornamentoDisponibilitaForm.folder eq 'Collocazioni'}">
							<td width="33%" class="schedaOn">
								<div align="center">Collocazione</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="33%" class="schedaOff">
								<div align="center">
									<html:submit property="methodAggDisp"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerCollocazione"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div>
							</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when
							test="${aggiornamentoDisponibilitaForm.folder eq 'Inventari'}">
							<td width="33%" class="schedaOn">
								<div align="center">Inventari</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="33%" class="schedaOff">
								<div align="center" styleId="etichetta">
									<html:submit property="methodAggDisp"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selPerInventari"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			<c:choose>
				<c:when
					test="${aggiornamentoDisponibilitaForm.folder eq 'RangeInv'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
				</c:when>
				<c:when
					test="${aggiornamentoDisponibilitaForm.folder eq 'Collocazioni'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
				</c:when>
				<c:when
					test="${aggiornamentoDisponibilitaForm.folder eq 'Inventari'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="divFooter">
			<!-- BOTTONIERA inserire solo i SOLO td -->
			<table align="center" border="0" style="height: 40px">
				<tr>
					<c:choose>
						<c:when test="${aggiornamentoDisponibilitaForm.disable == false}">
							<td><html:submit property="methodAggDisp">
									<bean:message key="documentofisico.bottone.ok"
										bundle="documentoFisicoLabels" />
								</html:submit> <html:submit property="methodAggDisp">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
						</c:when>
						<c:otherwise>
							<td><html:submit property="methodAggDisp">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
