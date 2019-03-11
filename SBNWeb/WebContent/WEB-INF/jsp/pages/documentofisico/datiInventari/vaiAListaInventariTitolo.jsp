<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/datiInventari/vaiAListaInventariTitolo.do">
		<div id="divForm"><!--<div id="divMessaggio"><sbn:errors	bundle="documentoFisicoMessages" /></div> -->
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="4">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text>
				<c:choose>
					<c:when
						test="${vaiAListaInventariTitoloForm.idInv ne 'EROGAZIONE_RICERCA' and vaiAListaInventariTitoloForm.idInv ne 'MOVIMENTI_UTENTE'}">
						<html:submit disabled="false" title="Lista Biblioteche"
							styleClass="buttonImageListaSezione" property="methodVaiAListaInv">
							<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
				</c:choose> <bs:write name="vaiAListaInventariTitoloForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<table width="100%" align="center">
				<tr valign="top">
					<td colspan="9"><bean:message key="documentofisico.notiziaCorrT"
						bundle="documentoFisicoLabels" />:&nbsp;<bs:write
						name="vaiAListaInventariTitoloForm" property="bidNotCorr" />&nbsp;&nbsp;<bs:write
						name="vaiAListaInventariTitoloForm" property="titoloNotCorr" /></td>
				</tr>
				<tr>
					<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
						totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
						parameter="methodVaiAListaInv"></sbn:blocchi>
				</tr>
			</table>
			<c:choose>
				<c:when
					test="${vaiAListaInventariTitoloForm.idInv ne 'EROGAZIONE_RICERCA' and vaiAListaInventariTitoloForm.idInv ne 'MOVIMENTI_UTENTE'}">
					<table width="100%" border="0">
						<tr class="etichetta" bgcolor="#dde8f0">
							<th>
							<div class="etichetta"><bean:message key="documentofisico.prg"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
							<div class="etichetta"><bean:message key="documentofisico.sezione"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
							<div class="etichetta"><bean:message key="documentofisico.collocazione"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
							<div class="etichetta"><bean:message key="documentofisico.specificazione"
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
							<th>
							<div class="etichetta"><bean:message key="documentofisico.situazioneAmmin"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th title="La collocazione è legata ad esemplare">
							<div class="etichetta">E</div>
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
							<div class="etichetta"><bean:message key="documentofisico.modalitaFruizioneT"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
							<div class="etichetta"><bean:message key="documentofisico.nonDisponibilePerT"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th width="3%">
							<div align="center"></div>
							</th>
						</tr>
						<l:notEmpty property="listaInventari" name="vaiAListaInventariTitoloForm">
							<l:iterate id="item" property="listaInventari"
								name="vaiAListaInventariTitoloForm" indexId="riga">
								<sbn:rowcolor var="color" index="riga" />
								<tr class="testoNormale">
									<td bgcolor="${color}">
									<sbn:anchor name="item" property="prg" />
									<div align="center" class="testoNormale"><sbn:linkbutton name="item"
										property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
										key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels"
										withAnchor="false" /></div>
									</td>
									<!--<bs:write name="item"	property="prg" /></div></td>-->
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codSez" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codLoc" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="specLoc" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="seqColl" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="precInv" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="sitAmm" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="flEsempl" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codSerie" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codInvent" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="fruizione" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="disponibilita" /></div>
									</td>
									<td bgcolor="${color}"><html:radio
										disabled="${vaiAListaInventariTitoloForm.disable}" property="selectedInv"
										value="${riga}" /></td>
								</tr>
							</l:iterate>
						</l:notEmpty>
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
							<div class="etichetta"><bean:message key="documentofisico.collocazione"
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
							<th>
							<div class="etichetta"><bean:message key="documentofisico.categoriaFruizioneT"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th>
							<div class="etichetta"><bean:message key="documentofisico.nonDisponibilePerT"
								bundle="documentoFisicoLabels" /></div>
							</th>
							<th width="3%">
							<div align="center"></div>
							</th>
						</tr>
						<l:notEmpty property="listaInventari" name="vaiAListaInventariTitoloForm">
							<l:iterate id="item" property="listaInventari"
								name="vaiAListaInventariTitoloForm" indexId="riga">
								<sbn:rowcolor var="color" index="riga" />
								<tr class="testoNormale">
									<td bgcolor="${color}">
									<sbn:anchor name="item" property="prg" />
									<div align="center" class="testoNormale"><sbn:linkbutton name="item"
										property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
										key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels"
										withAnchor="false" /></div>
									</td>
									<!--<bs:write name="item"	property="prg" /></div></td>-->
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codBib" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codSez" /><bs:write name="item"
										property="codLoc" /><bs:write name="item"
										property="specLoc" /><bs:write name="item"
										property="seqColl" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="codSerie" /><bs:write name="item"
										property="codInvent" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="precInv" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="fruizione" /></div>
									</td>
									<td bgcolor="${color}">
									<div align="center" class="testoNormale"><bs:write name="item"
										property="disponibilita" /></div>
									</td>
									<td bgcolor="${color}"><html:radio
										disabled="${vaiAListaInventariTitoloForm.disable}" property="selectedInv"
										value="${riga}" /></td>
								</tr>
							</l:iterate>
						</l:notEmpty>
					</table>
				</c:otherwise>
			</c:choose></div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodVaiAListaInv" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<c:choose>
					<c:when
						test="${vaiAListaInventariTitoloForm.idInv eq 'EROGAZIONE_RICERCA' or vaiAListaInventariTitoloForm.idInv eq 'MOVIMENTI_UTENTE'}">
						<td>
							<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.disponibilita"
									bundle="documentoFisicoLabels" />
							</html:submit>
						<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when
						test="${vaiAListaInventariTitoloForm.idInv eq 'STAMPA_SERVIZI_CORRENTI'}">
						<td><sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita><html:submit styleClass="pulsanti" property="methodVaiAListaInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when test="${vaiAListaInventariTitoloForm.conferma}">
						<td><html:submit styleClass="pulsanti" property="methodVaiAListaInv">
							<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodVaiAListaInv">
							<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:when test="${vaiAListaInventariTitoloForm.interrogazioneEsamina}">
						<td><sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.disponibilita"
									bundle="documentoFisicoLabels" />
							</html:submit>
							<sbn:checkAttivita idControllo="possessori">
								<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
									<bean:message key="documentofisico.bottone.possessori"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</sbn:checkAttivita>
						</sbn:checkAttivita></td>
					</c:when>
					<c:otherwise>
						<td><sbn:checkAttivita idControllo="df">
							<c:choose>
								<c:when test="${vaiAListaInventariTitoloForm.abilitaBottoneInviaInIndice}">
									<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAListaInv">
										<bean:message key="documentofisico.bottone.indice"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</c:when>
							</c:choose>
							<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.scegliColl"
									bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.altroInv"
									bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.cancInv" bundle="documentoFisicoLabels" />
							</html:submit>
							<sbn:checkAttivita idControllo="ordini">
								<html:submit styleClass="pulsanti" disabled="false" property="methodVaiAListaInv">
									<bean:message key="documentofisico.bottone.ordini" bundle="documentoFisicoLabels" />
								</html:submit>
							</sbn:checkAttivita>
							<sbn:checkAttivita idControllo="possessori">
								<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
									<bean:message key="documentofisico.bottone.possessori"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</sbn:checkAttivita>
							<sbn:checkAttivita idControllo="fascicoli">
								<html:submit styleClass="pulsanti" property="methodVaiAListaInv">
								<bean:message key="documentofisico.bottone.fascicoli"
									bundle="documentoFisicoLabels" />
								</html:submit>
							</sbn:checkAttivita>
						</sbn:checkAttivita><html:submit styleClass="pulsanti" property="methodVaiAListaInv">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
