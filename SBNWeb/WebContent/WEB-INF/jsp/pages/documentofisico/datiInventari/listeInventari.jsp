<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/datiInventari/listeInventari.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="3">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true"
					styleId="testoNormale" property="codBib" size="5" maxlength="3"></html:text>
				<bean-struts:write name="listeInventariForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<c:choose>
			<c:when
				test="${listeInventariForm.prov eq 'ordine' or  listeInventariForm.prov eq 'ordineIns' or  listeInventariForm.prov eq 'listaSuppInvOrd'}">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td class="etichetta"><bean:message key="documentofisico.ordineT"
							bundle="documentoFisicoLabels" /></td>
						<td colspan="7"></td>
					</tr>
					<tr>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codBibO" size="5" maxlength="3"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.tipoOrdineT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codTipoOrd" size="5" maxlength="1"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.annoOrdineT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="annoOrd" size="5" maxlength="4"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.numeroOrdineT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codOrd" size="15" maxlength="9"></html:text></div>
						</td>
					</tr>
					<tr>
						<td>
						<div><bean:message key="documentofisico.titoloOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td colspan="7"><bean-struts:write name="listeInventariForm"
							property="bidOrd" /><bean-struts:write name="listeInventariForm"
							property="isbdOrd" /></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.prov eq 'fattura'}">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td class="etichetta"><bean:message key="documentofisico.fatturaT"
							bundle="documentoFisicoLabels" /></td>
						<td colspan="7"></td>
					</tr>
					<tr>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codBibF" size="5" maxlength="3"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.annoFatturaT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="annoFattura" size="5" maxlength="4"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.progressivoT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="progrFattura" size="15" maxlength="9"></html:text></div>
						</td>
					</tr>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.prov eq 'rigaFattura'}">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<th colspan="8" align="left"><bean:message
							key="documentofisico.estremiRigaFatturaT" bundle="documentoFisicoLabels" /></th>
					</tr>
					<tr>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codBibF" size="5" maxlength="3"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.annoFatturaT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="annoFattura" size="5" maxlength="4"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.progressivoT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="progrFattura" size="12" maxlength="9"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.rigaT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="rigaFattura" size="12" maxlength="9"></html:text></div>
						</td>
					</tr>
					<tr>
						<th colspan="8" align="left"><bean:message
							key="documentofisico.estremiOrdineT" bundle="documentoFisicoLabels" /></th>
					</tr>
					<tr>
						<td><bean:message key="documentofisico.bibliotecaT"
							bundle="documentoFisicoLabels" /></td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codBibO" size="5" maxlength="3"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.tipoOrdineT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codTipoOrd" size="5" maxlength="1"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.annoOrdineT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="annoOrd" size="5" maxlength="4"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message
							key="documentofisico.numeroOrdineT" bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="codOrd" size="12" maxlength="9"></html:text></div>
						</td>
					</tr>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.prov eq 'listaSuppInvPoss'}">
				<table width="100%"
					style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
					<tr>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.pidT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="pid" size="15" maxlength="10"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.descrPidT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="etichetta"><html:text
							disabled="${listeInventariForm.disable}" styleId="testoNormale"
							property="descrPid" size="50" maxlength="30"></html:text></div>
						</td>
					</tr>
				</table>
				<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
					totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi" parameter="methodListeInv"></sbn:blocchi>
			</c:when>
		</c:choose> <c:choose>
			<c:when test="${listeInventariForm.tipoLista eq 'invNoncolloc'}">
				<table width="100%" border="0">
					<tr>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.serieT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="codSerie" size="15" maxlength="10"></html:text></div>
						</td>
					</tr>
				</table>
				<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
					totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi" parameter="methodListeInv"></sbn:blocchi>
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th colspan="2">
						<div class="etichetta"><bean:message key="documentofisico.titoloInventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.inventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.precInv"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th width="4%">
						<div align="center"></div>
						</th>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
								<div align="center" class="testoNormale"><bean-struts:write name="item"
									property="prg" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="bid" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="descr" /></div>
								</td>
								<td bgcolor="${color}">
								<div align="center" class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="precInv" /></div>
								</td>
								<td bgcolor="${color}"><html:radio property="selectedInv" value="${riga}" /></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.tipoLista eq 'listaInvDiColloc'}">
				<table width="100%" border="0">
					<tr>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.sezioneT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="codSez" size="15" maxlength="10"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.collocazioneT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="codLoc" size="30" maxlength="24" /></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.specificazioneT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="specLoc" size="20" maxlength="12" /></div>
						</td>
					</tr>
				</table>
				<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
					totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi" parameter="methodListeInv"></sbn:blocchi>
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th colspan="2">
						<div class="etichetta"><bean:message key="documentofisico.inventarioT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.sequenza"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.precInv"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<c:if test="${navForm.prov eq 'esamePeriodici' or navForm.prov eq 'fascicolo'}">
							<th>
								<div class="etichetta"><bean:message key="documentofisico.codiceOrdineT" bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
								<div class="etichetta"><bean:message key="documentofisico.annoAbbT" bundle="documentoFisicoLabels" /></div>
							</th>
						</c:if>
						<th colspan="2">
						<div class="etichetta"><bean:message key="documentofisico.titoloInventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<c:if test="${navForm.prov ne 'esamePeriodici'}">
							<th width="4%">
							<div align="center"></div>
							</th>
						</c:if>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
								<div align="center" class="testoNormale"><sbn:linkbutton name="item"
									property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
									key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels"
									checkAttivita="df"
									withAnchor="false" /></div>
								</td>
								<td bgcolor="${color}" width="4%">
								<div class="testoNormale"><bean-struts:write name="item" property="codSerie" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="seqColl" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="precInv" /></div>
								</td>
								<c:if test="${navForm.prov eq 'esamePeriodici' or navForm.prov eq 'fascicolo'}">
									<td bgcolor="${color}">
										<div class="testoNormale"><bean-struts:write name="item" property="chiaveOrdine" /></div>
									</td>
									<td bgcolor="${color}">
										<div class="testoNormale"><bean-struts:write name="item" property="annoAbb" /></div>
									</td>
								</c:if>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="bid" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="descr" /></div>
								</td>
								<c:if test="${navForm.prov ne 'esamePeriodici'}">
									<td bgcolor="${color}"><html:radio property="selectedInv" value="${riga}" /></td>
								</c:if>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.tipoLista eq 'listaInvDiCollocDaEsempl'}">
				<table width="100%" border="0">
					<tr>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.sezioneT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="codSez" size="15" maxlength="10"></html:text></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.collocazioneT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="codLoc" size="30" maxlength="24" /></div>
						</td>
						<td>
						<div class="etichetta"><bean:message key="documentofisico.specificazioneT"
							bundle="documentoFisicoLabels" /></div>
						</td>
						<td>
						<div class="testoNormale"><html:text disabled="true" styleId="testoNormale"
							property="specLoc" size="20" maxlength="12" /></div>
						</td>
					</tr>
				</table>
				<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
					totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi" parameter="methodListeInv"></sbn:blocchi>
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th colspan="2">
						<div class="etichetta"><bean:message key="documentofisico.inventarioT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<!--
						<th>
						<div class="etichetta"><bean:message
							key="documentofisico.situazioneAmmin"
							bundle="documentoFisicoLabels" /></div>
						</th>
						-->
						<th>
						<div class="etichetta"><bean:message key="documentofisico.precInv"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th colspan="2">
						<div class="etichetta"><bean:message key="documentofisico.titoloInventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th width="4%">
						<div align="center"></div>
						</th>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
								<div align="center" class="testoNormale"><sbn:linkbutton name="item"
									property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
									key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels"
									checkAttivita="df"
									withAnchor="false" /></div>
								</td>
								<td bgcolor="${color}" width="4%">
								<div class="testoNormale"><bean-struts:write name="item" property="codSerie" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<!--
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codSit" /></div>
								</td>
								-->
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="precInv" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="bid" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="descr" /></div>
								</td>
								<td bgcolor="${color}"><html:radio property="selectedInv" value="${riga}" /></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:when>
			<c:when
				test="${listeInventariForm.prov eq 'fattura' or listeInventariForm.prov eq 'rigaFattura'}">
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.biblioteca"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.serie"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.inventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<!--
						<th>
						<div class="etichetta"><bean:message
							key="documentofisico.sequenza" bundle="documentoFisicoLabels" /></div>
						</th>-->
						<th>
						<div class="etichetta"><bean:message key="documentofisico.precInvTitolo"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<c:choose>
							<c:when test="${listeInventariForm.prov eq 'rigaFattura'}">
								<th>
								<div class="etichetta"><bean:message key="documentofisico.prezzo"
									bundle="documentoFisicoLabels" /></div>
								</th>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when
								test="${listeInventariForm.prov eq 'rigaFattura' and listeInventariForm.tipoOperazione eq 'A'}">
								<th width="4%">
								<div align="center"></div>
								</th>
							</c:when>
						</c:choose>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
								<div align="center" class="testoNormale"><bean-struts:write name="item"
									property="prg" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="codBibO" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale" width="4%"><bean-struts:write name="item"
									property="codSerie" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<!--
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="seqColl" /></div>
								</td>
								-->
								<td bgcolor="${color}"><bean-struts:write name="item" property="precInv" />
								<bean-struts:write name="item" property="separatore" /> <bean-struts:write
									name="item" property="bid" /> <bean-struts:write name="item" property="descr" /></td>
								<c:choose>
									<c:when test="${listeInventariForm.prov eq 'rigaFattura'}">
										<td bgcolor="${color}">
										<div class="testoNormale"><bean-struts:write name="item"
											property="importo" /></div>
										</td>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when
										test="${listeInventariForm.prov eq 'rigaFattura' and listeInventariForm.tipoOperazione eq 'A'}">
										<td bgcolor="${color}">
										<div class="testoNormale"><html:radio property="selectedInv"
											value="${riga}" /></div>
										</td>
									</c:when>
								</c:choose>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.prov eq 'listaSuppInvPoss'}">
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.legame"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.notaLegame"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.serie"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.inventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.situazioneAmmin"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th colspan="2">
						<div class="etichetta"><bean:message key="documentofisico.titolo"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.natura"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th width="4%">
						<div align="center"></div>
						</th>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
								<div align="center" class="testoNormale"><sbn:linkbutton name="item"
									property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
									key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels"
									withAnchor="false" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="legame" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="notaColl" /></div>
								</td>
								<td bgcolor="${color}" width="4%">
								<div class="testoNormale"><bean-struts:write name="item" property="codSerie" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="sitAmm" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="bid" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="descr" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="natura" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><html:radio property="selectedInv" value="${riga}" /></div>
								</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:when>
			<c:when test="${listeInventariForm.prov eq 'listaOrdini' or listeInventariForm.prov eq 'listaOrdPeriodici'}">
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.tipoOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.annoOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.numeroOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.bidT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.titoloOrdineT"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.serie"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.inventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<c:if test="${listeInventariForm.prov eq 'listaOrdPeriodici' or listeInventariForm.prov eq 'listaSuppInvOrd'}">
							<th>
								<div class="etichetta"><bean:message key="documentofisico.annoAbbT" bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
							<div class="etichetta"><bean:message key="documentofisico.collocazioneT"
								bundle="documentoFisicoLabels" /></div>
							</th>
						</c:if>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.precInvTitolo"
							bundle="documentoFisicoLabels" /></div>
						</th>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
								<div align="center" class="testoNormale"><bean-struts:write name="item"
									property="prg" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codTipoOrd" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="annoOrd" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="codOrd" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="bidOrd" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="isbdOrd" /></div>
								</td>
								<td bgcolor="${color}" width="4%">
								<div class="testoNormale"><bean-struts:write name="item" property="codSerie" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<c:if test="${listeInventariForm.prov eq 'listaOrdPeriodici' or listeInventariForm.prov eq 'listaSuppInvOrd'}">
									<td bgcolor="${color}">
									<div class="testoNormale"><bean-struts:write name="item"
										property="annoAbb" /></div>
									</td>
									<td bgcolor="${color}">
									<div class="testoNormale"><bean-struts:write name="item"
										property="descrizioneColl" /></div>
									</td>
								</c:if>
								<td bgcolor="${color}"><bean-struts:write name="item" property="precInv" />
								<bean-struts:write name="item" property="separatore" /> <bean-struts:write
									name="item" property="bid" /> <bean-struts:write name="item" property="descr" /></td>
								<c:if test="${listeInventariForm.prov eq 'listaOrdPeriodici'}">
									<td bgcolor="${color}">
									<div class="testoNormale"><html:radio property="selectedInv" value="${riga}" /></div>
									</td>
								</c:if>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:when>
			<c:otherwise>
				<table width="100%" border="0">
					<tr class="etichetta" bgcolor="#dde8f0">
						<th>
						<div class="etichetta"><bean:message key="documentofisico.prg"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.biblioteca"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.serie"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.inventario"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<!--
						<th>
						<div class="etichetta"><bean:message
							key="documentofisico.sequenza" bundle="documentoFisicoLabels" /></div>
						</th>
						-->
						<c:if test="${listeInventariForm.prov eq 'listaSuppInvOrd' or listeInventariForm.prov eq 'ordine'}">
							<th>
								<div class="etichetta"><bean:message key="documentofisico.annoAbbT" bundle="documentoFisicoLabels" /></div>
							</th>
							<th><div class="etichetta"><bean:message key="documentofisico.collocazioneT"
								bundle="documentoFisicoLabels" /></div>
							</th>
						</c:if>
						<th>
						<div class="etichetta"><bean:message key="documentofisico.precInvTitolo"
							bundle="documentoFisicoLabels" /></div>
						</th>
						<c:choose>
							<c:when test="${listeInventariForm.tipoOperazione eq 'A'}">
								<th>
								<div class="etichetta"><bean:message key="documentofisico.prezzo"
									bundle="documentoFisicoLabels" /></div>
								</th>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${listeInventariForm.prov eq 'listaSuppInvOrd'}">
								<th width="4%">
								<div align="center"></div>
								</th>
							</c:when>
						</c:choose>
					</tr>
					<logic:notEmpty property="listaInventari" name="listeInventariForm">
						<logic:iterate id="item" property="listaInventari" name="listeInventariForm"
							indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale">
								<td bgcolor="${color}">
								<sbn:anchor name="item" property="prg" />
									<div align="center" class="testoNormale">
										<c:choose>
											<c:when test="${navForm.prov eq 'listaSuppInvOrd' or navForm.prov eq 'ordine'}">
												<sbn:linkbutton name="item"
													property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
													key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels"
													withAnchor="false" />
											</c:when>
											<c:otherwise>
												<bean-struts:write name="item" property="prg" />
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item" property="codBibO" /></div>
								</td>
								<td bgcolor="${color}" width="4%">
								<div class="testoNormale"><bean-struts:write name="item" property="codSerie" /></div>
								</td>
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="codInvent" /></div>
								</td>
								<!--
								<td bgcolor="${color}">
								<div class="testoNormale"><bean-struts:write name="item"
									property="seqColl" /></div>
								</td>
								-->
								<c:if test="${listeInventariForm.prov eq 'listaSuppInvOrd' or listeInventariForm.prov eq 'ordine'}">
									<td bgcolor="${color}" width="4%">
										<div class="testoNormale"><bean-struts:write name="item" property="annoAbb" /></div>
									</td>
									<td bgcolor="${color}">
										<div class="testoNormale"><bean-struts:write name="item" property="descrizioneColl" /></div>
									</td>
								</c:if>
								<td bgcolor="${color}"><bean-struts:write name="item" property="precInv" />
								<bean-struts:write name="item" property="separatore" /> <bean-struts:write
									name="item" property="bid" /> <bean-struts:write name="item" property="descr" /></td>
								<c:choose>
									<c:when test="${listeInventariForm.tipoOperazione eq 'A'}">
										<td bgcolor="${color}">
										<div class="testoNormale"><bean-struts:write name="item"
											property="importo" /></div>
										</td>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when test="${listeInventariForm.prov eq 'listaSuppInvOrd'}" >
										<td bgcolor="${color}">
										<div class="testoNormale"><html:radio property="selectedInv"
											value="${riga}" /></div>
										</td>
									</c:when>
								</c:choose>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</c:otherwise>
		</c:choose></div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodListeInv" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${listeInventariForm.prov eq 'listaSuppInvPoss'}">
						<td><html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.disponibilita"
								bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when test="${listeInventariForm.tipoLista eq 'invNoncolloc'}">
						<td><html:submit styleClass="pulsanti" disabled="false"
							property="methodListeInv">
							<bean:message key="documentofisico.bottone.esInv" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when
						test="${listeInventariForm.prov eq 'ordine'
									or listeInventariForm.prov eq 'ordineIns'
										or listeInventariForm.prov eq 'fattura'
										or listeInventariForm.prov eq 'listaSuppInvPoss'
											or (listeInventariForm.prov eq 'rigaFattura' and listeInventariForm.tipoOperazione eq 'V')}">
						<td><html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when
						test="${listeInventariForm.prov eq 'rigaFattura'
										and listeInventariForm.tipoOperazione eq 'A'}">
						<c:choose>
							<c:when test="${listeInventariForm.noInv}">
								<td><sbn:checkAttivita idControllo="df">
									<html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.nuovo" bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita></td>
								<td><html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</c:when>
							<c:otherwise>
								<td><sbn:checkAttivita idControllo="df">
									<html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.cancella"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita></td>
								<td><html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${listeInventariForm.prov eq 'listaSuppInvOrd'
										or listeInventariForm.prov eq 'esamePeriodici'
										or listeInventariForm.prov eq 'listaOrdPeriodici'
										or listeInventariForm.prov eq 'fascicolo'}">
						<c:choose>
							<c:when test="${listeInventariForm.conferma}">
								<table align="center">
									<tr>
										<td><html:submit styleClass="pulsanti" property="methodListeInv">
											<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
										</html:submit> <html:submit styleClass="pulsanti" property="methodListeInv">
											<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
										</html:submit></td>
									</tr>
								</table>
							</c:when>
							<c:otherwise>
								<c:if test="${listeInventariForm.prov ne 'esamePeriodici'}">
									<td><html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
									</html:submit></td>
								</c:if>
								<td><html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit></td>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${listeInventariForm.tipoLista eq 'listaInvDiColloc'}">
						<td><c:choose>
							<c:when test="${listeInventariForm.prov eq 'insColl'}">
								<sbn:checkAttivita idControllo="possessori">
									<html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.possessori"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita>
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:when test="${listeInventariForm.prov eq 'esaminaPosseduto'}">
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.disponibilita"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.possessori"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.possessori"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<!--<html:submit styleClass="pulsanti" disabled="false" property="methodListeInv">
									<bean:message key="documentofisico.bottone.esInv" bundle="documentoFisicoLabels" />
								</html:submit>
								-->
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.disponibilita"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.collDefin"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<sbn:checkAttivita idControllo="df">
									<html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.modificaInv"
											bundle="documentoFisicoLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.modificaColl"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita>
								<sbn:checkAttivita idControllo="etichette">
									<html:submit styleClass="pulsanti" property="methodListeInv">
										<bean:message key="documentofisico.bottone.etichetta"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita>
								<html:submit styleClass="pulsanti" disabled="false"	property="${navButtons}" >
									<bean:message key="documentofisico.bottone.moduloPrelievo" bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodListeInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose></td>
					</c:when>
					<c:when test="${listeInventariForm.tipoLista eq 'listaInvDiCollocDaEsempl'}">
						<td><html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.possessori"
								bundle="documentoFisicoLabels" />
						</html:submit><html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when test="${listeInventariForm.prov eq 'listaOrdini'}">
						<td><html:submit styleClass="pulsanti" property="methodListeInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
