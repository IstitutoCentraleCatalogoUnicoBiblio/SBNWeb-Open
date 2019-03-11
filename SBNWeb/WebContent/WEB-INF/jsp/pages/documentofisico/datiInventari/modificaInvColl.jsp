<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/datiInventari/modificaInvColl.do">
		<html:hidden property="action" />
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="3">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text readonly="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text> <bean-struts:write
					name="modificaInvCollForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<table width="100%"
			style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
			<tr>
				<td>
				<div class="etichetta"><bean:message key="documentofisico.notiziaCorrT"
					bundle="documentoFisicoLabels" />: <bean-struts:write name="modificaInvCollForm"
					property="bid" />&nbsp;&nbsp;<bean-struts:write name="modificaInvCollForm"
					property="isbd" /></div>
				</td>
			</tr>
			<c:choose>
				<c:when test="${modificaInvCollForm.recColl ne null}">
					<tr>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.bidCollocazioneT"
							bundle="documentoFisicoLabels" />: <bean-struts:write name="modificaInvCollForm"
							property="bidColloc" />&nbsp;&nbsp;<bean-struts:write name="modificaInvCollForm"
							property="isbdCollocazione" /></div>
						</td>
					</tr>
				</c:when>
			</c:choose>
		</table>
		<br>
		<table width="100%"
			style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
			<c:choose>
				<c:when
					test="${modificaInvCollForm.prov eq 'ordine' or modificaInvCollForm.prov eq 'ordineIns'}">
					<table width="100%"
						style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
						<tr>
							<td class="etichetta"><bean:message key="documentofisico.ordineT"
								bundle="documentoFisicoLabels" /></td>
							<td colspan="7"></td>
						</tr>
						<tr>
							<td>
							<div class="etichetta"><bean:message key="documentofisico.tipoOrdineT"
								bundle="documentoFisicoLabels" /></div>
							</td>
							<td>
							<div class="etichetta"><html:text disabled="${modificaInvCollForm.disable}"
								styleId="testoNormale" property="recInv.codTipoOrd" size="5" maxlength="1"></html:text></div>
							</td>
							<td>
							<div class="etichetta"><bean:message key="documentofisico.annoOrdineT"
								bundle="documentoFisicoLabels" /></div>
							</td>
							<td>
							<div class="etichetta"><html:text disabled="${modificaInvCollForm.disable}"
								styleId="testoNormale" property="recInv.annoOrd" size="5" maxlength="4"></html:text></div>
							</td>
							<td>
							<div class="etichetta"><bean:message key="documentofisico.numeroOrdineT"
								bundle="documentoFisicoLabels" /></div>
							</td>
							<td>
							<div class="etichetta"><html:text disabled="${modificaInvCollForm.disable}"
								styleId="testoNormale" property="recInv.codOrd" size="15" maxlength="9"></html:text></div>
							</td>
							<td>
							<div class="etichetta"><bean:message key="documentofisico.fornitoreT"
								bundle="documentoFisicoLabels" /></div>
							</td>
							<td>
							<div class="etichetta"><html:text disabled="${modificaInvCollForm.disable}"
								styleId="testoNormale" property="recInv.codFornitore" size="15" maxlength="9"></html:text></div>
							</td>
						</tr>
					</table>
				</c:when>
				<c:otherwise>
					<table width="100%">
						<c:choose>
							<c:when test="${modificaInvCollForm.recColl ne null}">
								<tr>
									<td width="20%">
									<div class="etichetta"><bean:message key="documentofisico.collocazioneT"
										bundle="documentoFisicoLabels" /></div>
									</td>
									<td colspan="3">
									<div><html:text styleId="testoNormale" property="codSez"
										disabled="${modificaInvCollForm.disable}" size="15" maxlength="10">
									</html:text> <html:text styleId="testoNormale" property="codColloc"
										disabled="${modificaInvCollForm.disable}" size="30" maxlength="24"></html:text>
									<c:choose>
										<c:when test="${modificaInvCollForm.tipoCollocN ne 'N'}">
											<html:text styleId="testoNormale" property="specColloc"
												disabled="${modificaInvCollForm.disable}" size="15" maxlength="12">
											</html:text>
										</c:when>
									</c:choose></div>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>
									<div class="etichetta"><bean:message key="documentofisico.consistenzaCollT"
										bundle="documentoFisicoLabels" /></div>
									</td>
									<td colspan="4"><html:textarea styleId="testoNormale" cols="80" rows=""
										disabled="${modificaInvCollForm.disableConsist}" property="recColl.consistenza"></html:textarea></td>
								</tr>
								<tr><!--
									<td>
									<div class="etichetta"><bean:message key="documentofisico.stampaEtichettaT"
										bundle="documentoFisicoLabels" /></div>
									</td>
									<td colspan="2"><html:checkbox property="stampaEtich"
										disabled="${modificaInvCollForm.disablePerModInvDaNav}"></html:checkbox><html:hidden
										property="stampaEtich" value="false" /></td>
								</tr>
							--></c:when>
						</c:choose>
					</table>
				</c:otherwise>
			</c:choose>
		</table>
		<table>
			<tr>
				<td>
				<div class="etichetta"><bean:message key="documentofisico.stampaEtichettaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2"><html:checkbox property="stampaEtich"
					disabled="${modificaInvCollForm.disablePerModInvDaNav}"></html:checkbox><html:hidden
					property="stampaEtich" value="false" /></td>
			</tr>
		</table>
		<br>
		<c:choose>
			<c:when test="${modificaInvCollForm.prov ne 'visualizzazionePerCV'}">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr height="30">
						<c:choose>
							<c:when test="${modificaInvCollForm.folder eq 'tab1'}">
								<td width="40%" class="schedaOn"><c:choose>
									<c:when test="${modificaInvCollForm.prov eq 'ordine'}">
										<div align="center"><bean:message key="documentofisico.modInvCollTab1"
											bundle="documentoFisicoLabels" /><bean-struts:write name="modificaInvCollForm"
											property="recInv.codBibO" /><bean-struts:write name="modificaInvCollForm"
											property="recInv.codSerie" /> <bean-struts:write name="modificaInvCollForm"
											property="recInv.codInvent" /> ( <bean-struts:write name="modificaInvCollForm"
											property="index" /> di <bean-struts:write name="modificaInvCollForm"
											property="numInv" /> )</div>
									</c:when>
									<c:otherwise>
										<div align="center"><bean:message key="documentofisico.modInvCollTab1"
											bundle="documentoFisicoLabels" /><bean-struts:write name="modificaInvCollForm"
											property="recInv.codSerie" /> <bean-struts:write name="modificaInvCollForm"
											property="recInv.codInvent" /></div>
									</c:otherwise>
								</c:choose></td>
							</c:when>
							<c:otherwise>
								<td width="40%" class="schedaOff"><c:choose>
									<c:when
										test="${modificaInvCollForm.prov eq 'ordine' or modificaInvCollForm.prov eq 'ordineIns'}">
										<div align="center"><html:submit property="methodModInvColl"
											styleClass="sintButtonLinkDefault"
											disabled="${modificaInvCollForm.disablePerModInvDaNav}">
											<bean:message key="documentofisico.modInvCollTab1"
												bundle="documentoFisicoLabels" />
										</html:submit><bean-struts:write name="modificaInvCollForm" property="recInv.codBibO" /> <bean-struts:write
											name="modificaInvCollForm" property="recInv.codSerie" /> <bean-struts:write
											name="modificaInvCollForm" property="recInv.codInvent" /> ( <bean-struts:write
											name="modificaInvCollForm" property="index" /> di <bean-struts:write
											name="modificaInvCollForm" property="numInv" /> )</div>
									</c:when>
									<c:otherwise>
										<div align="center"><html:submit property="methodModInvColl"
											styleClass="sintButtonLinkDefault"
											disabled="${modificaInvCollForm.disablePerModInvDaNav}">
											<bean:message key="documentofisico.modInvCollTab1"
												bundle="documentoFisicoLabels" />
										</html:submit><bean-struts:write name="modificaInvCollForm" property="recInv.codSerie" /> <bean-struts:write
											name="modificaInvCollForm" property="recInv.codInvent" /></div>
									</c:otherwise>
								</c:choose></td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${modificaInvCollForm.folder eq 'tab11'}">
								<td width="20%" class="schedaOn">
								<div align="center">Copia Digitale</div>
								</td>
							</c:when>
							<c:otherwise>
								<td width="20%" class="schedaOff">
								<div align="center"><html:submit property="methodModInvColl"
									styleClass="sintButtonLinkDefault"
									disabled="${modificaInvCollForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.modInvCollTab11"
										bundle="documentoFisicoLabels" />
								</html:submit></div>
								</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${modificaInvCollForm.folder eq 'tab2'}">
								<td width="20%" class="schedaOn">
								<div align="center">Carico inventariale / Fattura</div>
								</td>
							</c:when>
							<c:otherwise>
								<td width="20%" class="schedaOff">
								<div align="center"><html:submit property="methodModInvColl"
									styleClass="sintButtonLinkDefault"
									disabled="${modificaInvCollForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.modInvCollTab2" bundle="documentoFisicoLabels" />
								</html:submit></div>
								</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${modificaInvCollForm.folder eq 'tab3'}">
								<td width="20%" class="schedaOn">
								<div align="center">Scarico Inventariale</div>
								</td>
							</c:when>
							<c:otherwise>
								<td width="20%" class="schedaOff">
								<div align="center" styleId="etichetta"><html:submit
									property="methodModInvColl" styleClass="sintButtonLinkDefault"
									disabled="${modificaInvCollForm.disablePerModInvDaNav}">
									<bean:message key="documentofisico.modInvCollTab3" bundle="documentoFisicoLabels" />
								</html:submit></div>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</table>
				<br>
				<c:choose>
					<c:when test="${modificaInvCollForm.folder eq 'tab1'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/modificaInvCollTab1.jsp" />
					</c:when>
					<c:when test="${modificaInvCollForm.folder eq 'tab11'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/modificaInvCollTab11.jsp" />
					</c:when>
					<c:when test="${modificaInvCollForm.folder eq 'tab2'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/modificaInvCollTab2.jsp" />
					</c:when>
					<c:when test="${modificaInvCollForm.folder eq 'tab3'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/documentofisico/datiInventari/modificaInvCollTab3.jsp" />
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</c:when>
		</c:choose></div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${modificaInvCollForm.conferma}">
						<td><html:submit styleClass="pulsanti" property="methodModInvColl">
							<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodModInvColl">
							<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><c:choose>
							<c:when test="${modificaInvCollForm.abilitaBottoneInviaInIndice}">
								<html:submit styleClass="pulsanti" disabled="false" property="methodModInvColl">
									<bean:message key="documentofisico.bottone.indice" bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
						</c:choose> <c:choose>
							<c:when test="${modificaInvCollForm.invColl}">
								<c:choose>
									<c:when test="${modificaInvCollForm.prov eq 'visualizzazionePerCV'}">
										<html:submit styleClass="pulsanti" property="methodModInvColl">
											<bean:message key="documentofisico.bottone.indietro"
												bundle="documentoFisicoLabels" />
										</html:submit>
									</c:when>
									<c:otherwise>
										<html:submit styleClass="pulsanti" property="methodModInvColl"
											disabled="${modificaInvCollForm.disableTastoEsemplare}">
											<bean:message key="documentofisico.bottone.esemplare"
												bundle="documentoFisicoLabels" />
										</html:submit>
										<html:submit styleClass="pulsanti" property="methodModInvColl"
											disabled="${modificaInvCollForm.disableTastoCancInv}">
											<bean:message key="documentofisico.bottone.cancInv"
												bundle="documentoFisicoLabels" />
										</html:submit>
										<html:submit styleClass="pulsanti" property="methodModInvColl"
											disabled="${modificaInvCollForm.disableTastoSalva}">
											<bean:message key="documentofisico.bottone.salva"
												bundle="documentoFisicoLabels" />
										</html:submit>
										<html:submit styleClass="pulsanti" property="methodModInvColl">
											<bean:message key="documentofisico.bottone.indietro"
												bundle="documentoFisicoLabels" />
										</html:submit>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when
								test="${modificaInvCollForm.prov eq 'ordine' or modificaInvCollForm.prov eq 'ordineIns'}">
								<html:submit styleClass="pulsanti" property="methodModInvColl">
									<bean:message key="documentofisico.bottone.salva" bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodModInvColl">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit styleClass="pulsanti" property="methodModInvColl"
									disabled="${modificaInvCollForm.disableTastoCancInv}">
									<bean:message key="documentofisico.bottone.cancInv"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodModInvColl"
									disabled="${modificaInvCollForm.disableTastoSalva}">
									<bean:message key="documentofisico.bottone.salva" bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodModInvColl">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
