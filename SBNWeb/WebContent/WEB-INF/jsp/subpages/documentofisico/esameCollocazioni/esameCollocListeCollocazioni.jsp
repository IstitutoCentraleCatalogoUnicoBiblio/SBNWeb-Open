<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<div id="divForm">
<div id="divMessaggio"><sbn:errors bundle="documentoFisicoMessages" /></div>
<table width="100%" border="0">
	<tr>
		<td>
		<div align="left" class="testo"><bean:message
			key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /> <html:text
			disabled="true" styleId="testoNormale" property="codBib" size="5"
			maxlength="3"></html:text> <bs:write
			name="esameCollocListeCollocazioniForm" property="descrBib" /></div>
		</td>
	</tr>
</table>
<c:choose>
	<c:when
		test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaSuppColl'}">
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="documentofisico.sezioneT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true"
					styleId="testoNormale" property="codSezione" size="15" maxlength="10"></html:text>
				</td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodEsameCollListeColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.collocazioni" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.numOccorr" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.totInv" bundle="documentoFisicoLabels" /></div>
				</th>
				<th width="4%"></th>
			</tr>
			<l:iterate id="item" property="listaCollocSpec" indexId="listaIdx"
				name="esameCollocListeCollocazioniForm">
				<sbn:rowcolor var="color" index="listaIdx" />
				<tr bgcolor="#FFCC99">
					<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale"><bs:write
							name="item" property="prg" /></div>
					</td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="codColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="numOccorrenze" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="totInventari" /></td>
					<td bgcolor="${color}" class="testoNormale">
					<div align="center"><html:radio property="selectedCollSpec"
						value="${listaIdx}" /></div>
					</td>
				</tr>
			</l:iterate>
		</table>
	</c:when>
	<c:when
		test="${esameCollocListeCollocazioniForm.listaSupportoSpec eq 'listaSuppSpec'}">
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="documentofisico.sezioneT"
					bundle="documentoFisicoLabels" /><html:text disabled="true"
					styleId="testoNormale" property="codSezione" size="15" maxlength="10"></html:text></td>
				<td><bean:message key="documentofisico.collocazioneT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true"
					styleId="testoNormale" property="codColloc" size="30" maxlength="24"></html:text></td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodEsameCollListeColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.specificazioni" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.numOccorr" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.totInv" bundle="documentoFisicoLabels" /></div>
				</th>
				<th width="4%"></th>
			</tr>
			<l:iterate id="item" property="listaCollocSpec" indexId="listaIdx"
				name="esameCollocListeCollocazioniForm">
				<sbn:rowcolor var="color" index="listaIdx" />
				<tr bgcolor="#FFCC99">
					<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale"><bs:write
							name="item" property="prg" /></div>
					</td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="specColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="numOccorrenze" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="totInventari" /></td>
					<td bgcolor="${color}" class="testoNormale">
					<div align="center"><html:radio property="selectedCollSpec"
						value="${listaIdx}" /></div>
					</td>
				</tr>
			</l:iterate>
		</table>
	</c:when>
	<c:when
		test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaUltColl'}">
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="documentofisico.sezioneT"
					bundle="documentoFisicoLabels" /><html:text disabled="true"
					styleId="testoNormale" property="codSezione" size="15" maxlength="10"></html:text></td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodEsameCollListeColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.collocazioni" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.numOccorr" bundle="documentoFisicoLabels" /></div>
				</th>
				<th width="4%"></th>
			</tr>
			<l:iterate id="item" property="listaCollocSpec" indexId="listaIdx"
				name="esameCollocListeCollocazioniForm">
				<sbn:rowcolor var="color" index="listaIdx" />
				<tr bgcolor="#FFCC99">
					<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale"><bs:write
							name="item" property="prg" /></div>
					</td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="codColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="numOccorrenze" /></td>
					<td bgcolor="${color}" class="testoNormale">
					<div align="center"><html:radio property="selectedCollSpec"
						value="${listaIdx}" /></div>
					</td>
				</tr>
			</l:iterate>
		</table>
	</c:when>
	<c:when
		test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaUltSpec'}">
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="documentofisico.sezioneT"
					bundle="documentoFisicoLabels" /><html:text disabled="true"
					styleId="testoNormale" property="codSezione" size="15" maxlength="10"></html:text></td>
				<td><bean:message key="documentofisico.collocazioneT"
					bundle="documentoFisicoLabels" /><html:text disabled="true"
					styleId="testoNormale" property="codColloc" size="30" maxlength="24"></html:text></td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodEsameCollListeColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.specificazioni" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.numOccorr" bundle="documentoFisicoLabels" /></div>
				</th>
			</tr>
			<l:iterate id="item" property="listaCollocSpec" indexId="listaIdx"
				name="esameCollocListeCollocazioniForm">
				<sbn:rowcolor var="color" index="listaIdx" />
				<tr bgcolor="#FFCC99">
					<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale"><bs:write
							name="item" property="prg" /></div>
					</td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="specColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="numOccorrenze" /></td>
				</tr>
			</l:iterate>
		</table>
	</c:when>
	<c:when
		test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaCollEsemplare'}">
		<table width="100%" border="0">
			 <tr>
				<%--<c:choose>
					<c:when test="${esameCollocListeCollocazioniForm.prov eq 'posseduto'}">
						<td></td>
						<td></td>
					</c:when>
					<c:otherwise>
						<td><bean:message key="documentofisico.sezioneT"
							bundle="documentoFisicoLabels" /><html:text disabled="true"
							styleId="testoNormale" property="codSezione" size="15" maxlength="10"></html:text></td>
					</c:otherwise>
				</c:choose>--%>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodEsameCollListeColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<%--<c:choose>
					<c:when test="${esameCollocListeCollocazioniForm.prov eq 'posseduto'}"> --%>
						<th>
						<div align="center" class="etichetta"><bean:message
							key="documentofisico.sezione" bundle="documentoFisicoLabels" /></div>
						</th>
					<%--</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>--%>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.collocazione" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.specificazione" bundle="documentoFisicoLabels" /></div>
				</th>
				<th colspan="2">
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.titoloColl" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.invColl" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.consistenza" bundle="documentoFisicoLabels" /></div>
				</th>
				<th title="La collocazione è legata ad esemplare">
				<div align="center" class="etichetta">E</div>
				</th>
				<th width="3%">
				<div align="center" class="etichetta"></div>
				</th>
			</tr>
			<l:iterate id="item" property="listaColloc" indexId="listaIdx"
				name="esameCollocListeCollocazioniForm">
				<sbn:rowcolor var="color" index="listaIdx" />
				<tr bgcolor="#FFCC99">
					<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale"><bs:write
							name="item" property="prg" /></div>
					</td>
					<%--<c:choose>
						<c:when test="${esameCollocListeCollocazioniForm.prov eq 'posseduto'}">--%>
							<td bgcolor="${color}" class="testoNormale"><bs:write
								name="item" property="codSez" /></td>
						<%--</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>--%>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="codColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="specColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="bid" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="titolo" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="totInv" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="consistenza" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="flgEsemplare" /></td>
					<td bgcolor="${color}" class="testoNormale">
					<div align="center"><html:radio property="selectedCollSpec"
						value="${listaIdx}" /></div>
					</td>
				</tr>
			</l:iterate>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="documentofisico.sezioneT" bundle="documentoFisicoLabels" /><html:text
					disabled="true" styleId="testoNormale" property="codSezione" size="15" maxlength="10"></html:text></td>
				<td><bean:message key="documentofisico.collocazioneT"
					bundle="documentoFisicoLabels" /><html:text disabled="true" styleId="testoNormale"
					property="paramRicerca.codLoc" size="50" maxlength="48"></html:text></td>
				<td><bean:message key="documentofisico.specificazioneT"
					bundle="documentoFisicoLabels" /><html:text disabled="true" styleId="testoNormale"
					property="paramRicerca.codSpec" size="30" maxlength="24"></html:text></td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodEsameCollListeColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<th colspan="2">
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.titoloColl" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.collocazione" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.specificazione" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.invColl" bundle="documentoFisicoLabels" /></div>
				</th>
				<th>
				<div align="center" class="etichetta"><bean:message
					key="documentofisico.consistenza" bundle="documentoFisicoLabels" /></div>
				</th>
				<th title="La collocazione è legata ad esemplare">
				<div align="center" class="etichetta">E</div>
				</th>
				<th width="3%">
				<div align="center" class="etichetta"></div>
				</th>
			</tr>
			<l:iterate id="item" property="listaColloc" indexId="listaIdx"
				name="esameCollocListeCollocazioniForm">
				<sbn:rowcolor var="color" index="listaIdx" />
				<tr bgcolor="#FFCC99">
					<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale">
							<sbn:linkbutton name="item" index="listaIdx" value="prg"
								property="selectedCollSpec"
								key="documentofisico.bottone.inventari"
								bundle="documentoFisicoLabels" />
						</div>
					</td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="bid" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="titolo" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="codColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="specColloc" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="totInv" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="consistenza" /></td>
					<td bgcolor="${color}" class="testoNormale"><bs:write
						name="item" property="flgEsemplare" /></td>
					<td bgcolor="${color}" class="testoNormale">
					<div align="center"><html:radio property="selectedCollSpec"
						value="${listaIdx}" /></div>
					</td>
				</tr>
			</l:iterate>
		</table>
	</c:otherwise>
</c:choose></div>
<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
	totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
	parameter="methodEsameCollListeColl" bottom="true"></sbn:blocchi>
<div id="divFooterCommon"><c:choose>
	<c:when test="${esameCollocListeCollocazioniForm.listaTipiOrdinamento eq null}">
	</c:when>
	<c:otherwise>
		<table width="100%">
			<tr>
				<td class="etichetta"><bean:message key="documentofisico.ordinamento"
					bundle="documentoFisicoLabels" /></td>
				<td class="testoNormale"><html:select property="tipoOrdinam">
					<html:optionsCollection property="listaTipiOrdinamento" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
	</c:otherwise>
</c:choose></div>
<div id="divFooter">
<table align="center">
	<tr>
		<td><c:choose>
			<c:when
				test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaSuppColl'}">
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.scegli"
						bundle="documentoFisicoLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
			<c:when
				test="${esameCollocListeCollocazioniForm.listaSupportoSpec eq 'listaSuppSpec'}">
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.scegli"
						bundle="documentoFisicoLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
			<c:when
				test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaUltColl'}">
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.lsUS" bundle="documentoFisicoLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
			<c:when
				test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaUltSpec'}">
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
			<c:when
				test="${esameCollocListeCollocazioniForm.listaSupportoColl eq 'listaCollEsemplare'}">
				<c:if test="${navForm.periodici}">
					<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
						<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
					</html:submit>
				</c:if>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.inventari"
						bundle="documentoFisicoLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.inventari"
						bundle="documentoFisicoLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.esemplare"
						bundle="documentoFisicoLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameCollListeColl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>
			</c:otherwise>
		</c:choose></td>
	</tr>
</table>
</div>
