<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/datiInventari/vaiAInserimentoInv.do">
		<html:hidden property="test" />
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="3">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text>
					<c:choose>
						<c:when	test="${vaiAInserimentoInvForm.prov eq 'ordine' or vaiAInserimentoInvForm.prov eq 'ordineIns'}">
							&nbsp;
						</c:when>
						<c:otherwise>
							<html:submit disabled="false" title="Lista Biblioteche"
								styleClass="buttonImageListaSezione" property="methodVaiAInsInv">
								<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
							</html:submit>
						</c:otherwise>
					</c:choose><bean-struts:write name="vaiAInserimentoInvForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<table width="100%" align="center">
			<tr valign="top">
				<td class="etichetta"><bean:message key="documentofisico.notiziaCorrT"
					bundle="documentoFisicoLabels" />: <span class="etichetta"></span> <bean-struts:write
					name="vaiAInserimentoInvForm" property="bid" />&nbsp;&nbsp;<bean-struts:write
					name="vaiAInserimentoInvForm" property="titolo" /></td>
			</tr>
		</table>
		<c:choose>
			<c:when
				test="${vaiAInserimentoInvForm.prov eq 'ordine' or vaiAInserimentoInvForm.prov eq 'ordineIns'}">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td class="etichetta"><bean:message key="documentofisico.ordineT"
							bundle="documentoFisicoLabels" /></td>
						<td colspan="7"></td>
					</tr>
					<tr>
						<td>
						<div class="etichetta" valign="top"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text disabled="${vaiAInserimentoInvForm.disable}"
							styleId="testoNormale" property="codBibO" size="5" maxlength="3"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.tipoOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text disabled="${vaiAInserimentoInvForm.disable}"
							styleId="testoNormale" property="codTipoOrd" size="5" maxlength="1"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.annoOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text disabled="${vaiAInserimentoInvForm.disable}"
							styleId="testoNormale" property="annoOrd" size="5" maxlength="4"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.numeroOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text disabled="${vaiAInserimentoInvForm.disable}"
							styleId="testoNormale" property="codOrd" size="15" maxlength="9"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.fornitoreT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text disabled="${vaiAInserimentoInvForm.disable}"
							styleId="testoNormale" property="codFornitore" size="15" maxlength="9"></html:text></div>
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="documentofisico.inventarioT"
							bundle="documentoFisicoLabels" /></td>
						<td colspan="7"></td>
					</tr>
					<tr>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.serieT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:select property="recInv.codSerie">
							<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
						</html:select></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.nInventariT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text styleId="testoNormale"
							property="recInv.numInv" size="3" maxlength="2"></html:text></div>
						</td>
						<td colspan="3">
						<noscript><html:submit styleClass="pulsanti" disabled="false"
							property="methodVaiAInsInv">
							<bean:message key="documentofisico.aggiorna" bundle="documentoFisicoLabels" />
						</html:submit></noscript>
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table width="100%" border="0">
					<tr>
						<td>
						<table width="100%" border="0">
							<tr>
								<td>
								<div class="testoNormale"><bean:message
									key="documentofisico.nonPresenteNelSistema" bundle="documentoFisicoLabels" /></div>
								</td>
								<td>
								<div class="testoNormale"><html:radio property="tipoOperazione" value="N"
									onchange="validateSubmit('test', 'tipoOp');" /></div>
								</td>
							</tr>
							<tr>
								<td>
								<div class="testoNormale"><bean:message
									key="documentofisico.presenteNelSistema" bundle="documentoFisicoLabels" /></div>
								</td>
								<td>
								<div class="testoNormale"><html:radio property="tipoOperazione" value="S"
									onchange="validateSubmit('test', 'tipoOp');" /></div>
								</td>
							</tr>
							<tr>
								<td>
								<div class="testoNormale"><bean:message
									key="documentofisico.progrAutomatico" bundle="documentoFisicoLabels" /></div>
								</td>
								<td>
								<div class="testoNormale"><html:radio property="tipoOperazione" value="C"
									onchange="validateSubmit('test', 'tipoOp');" /></div>
								</td>
							</tr>
							<tr>
								<td>
								<div class="testoNormale"><bean:message key="documentofisico.vecchioAccess"
									bundle="documentoFisicoLabels" /></div>
								</td>
								<td>
								<div class="testoNormale"><html:radio property="tipoOperazione" value="P"
									onchange="validateSubmit('test', 'tipoOp');" /></div>
								</td>
							</tr>
						</table>
						</td>
						<td width="72%" valign="top">
						<table>
							<tr>
								<td width="15%">
								<noscript><html:submit styleClass="pulsanti" disabled="false"
									property="methodVaiAInsInv">
									<bean:message key="documentofisico.aggiorna" bundle="documentoFisicoLabels" />
								</html:submit></noscript>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td width="20%"><bean:message key="documentofisico.serieT"
							bundle="documentoFisicoLabels" /></td>
						<td width="10%"><html:select property="recInv.codSerie"
							onchange="validateSubmit('test', 'serie');">
							<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
						</html:select></td>
						<td width="30%">
						<div class="etichetta"><bean:message key="documentofisico.numeroT"
							bundle="documentoFisicoLabels" /><html:text
							disabled="${vaiAInserimentoInvForm.disable}" styleId="testoNormale"
							property="recInv.codInvent" size="15" maxlength="9"></html:text></div>
						</td>
						<td colspan="2"><!--<div class="etichetta"><bean:message key="documentofisico.dataIngressoT"
							bundle="documentoFisicoLabels" /><html:text styleId="testoNormale"
							disabled="${vaiAInserimentoInvForm.disableDataIngresso}"
							property="recInv.dataIngresso" size="15" maxlength="10"></html:text></div>
						--></td>
					</tr>
					<tr>
						<td><bean:message key="documentofisico.precisazioneVolumeT"
							bundle="documentoFisicoLabels" /></td>
						<td colspan="3"><html:textarea styleId="testoNormale" property="recInv.precInv"
							cols="100" rows="5"></html:textarea></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td --> <c:choose>
			<c:when test="${vaiAInserimentoInvForm.conferma}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodVaiAInsInv">
							<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAInsInv">
							<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table align="center">
					<tr>
						<c:choose>
							<c:when
								test="${vaiAInserimentoInvForm.prov eq 'ordine' or vaiAInserimentoInvForm.prov eq 'ordineIns'}">
								<td><html:submit styleClass="pulsanti" property="methodVaiAInsInv">
									<bean:message key="documentofisico.bottone.crea" bundle="documentoFisicoLabels" />
								</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAInsInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</c:when>
							<c:otherwise>
								<td><sbn:checkAttivita idControllo="df">
									<sbn:checkAttivita idControllo="ordini">
										<html:submit styleClass="pulsanti" property="methodVaiAInsInv">
											<bean:message key="documentofisico.bottone.ordini"
												bundle="documentoFisicoLabels" />
										</html:submit>
									</sbn:checkAttivita>
									<html:submit styleClass="pulsanti" property="methodVaiAInsInv">
										<bean:message key="documentofisico.bottone.creaecolloca"
											bundle="documentoFisicoLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" property="methodVaiAInsInv">
										<bean:message key="documentofisico.bottone.crea" bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita><html:submit styleClass="pulsanti" property="methodVaiAInsInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
