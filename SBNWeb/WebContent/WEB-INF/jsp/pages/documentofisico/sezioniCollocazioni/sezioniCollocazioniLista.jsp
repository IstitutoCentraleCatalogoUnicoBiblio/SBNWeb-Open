<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/sezioniCollocazioni/sezioniCollocazioniLista.do">
		<div id="divForm"><html:hidden property="action" />
		<div id="divMessaggio"><sbn:errors /></div>
		<c:choose>
			<c:when test="${sezioniCollocazioniListaForm.richiamo eq 'lista'}">
				<table width="100%" border="0">
					<tr>
						<td colspan="3">
						<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
							bundle="documentoFisicoLabels" /> <html:text disabled="true"
							styleId="testoNormale" property="codBib" size="5" maxlength="3"></html:text> <bean-struts:write
							name="sezioniCollocazioniListaForm" property="descrBib" /></div>
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table width="100%" border="0">
					<tr>
						<td colspan="4">
						<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
							bundle="documentoFisicoLabels" /> <html:text disabled="true"
							styleId="testoNormale" property="codBib" size="5" maxlength="3"></html:text> <span
							disabled="true"> <html:submit title="Lista Biblioteche"
							styleClass="buttonImageListaSezione" disabled="false" property="methodSezColl">
							<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
						</html:submit></span> <bean-struts:write name="sezioniCollocazioniListaForm" property="descrBib" /></div>
						</td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose> <br>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchiSez" parameter="methodSezColl"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th class="etichetta" scope="col"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.sezione"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.tipoSezione"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message
					key="documentofisico.tipoCollocazione" bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.inventari"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.descrizione"
					bundle="documentoFisicoLabels" /></th>
				<th scope="col"></th>
			</tr>
			<logic:notEmpty property="listaSezioni" name="sezioniCollocazioniListaForm">
				<logic:iterate id="item" property="listaSezioni" name="sezioniCollocazioniListaForm"
					indexId="listaIdx">
					<sbn:rowcolor var="color" index="listaIdx" />
					<tr bgcolor="#FFCC99">
						<td bgcolor="${color}" class="testoNormale">
							<sbn:anchor name="item" property="prg" />
							<bean-struts:write name="item" property="prg" /></td>
						<c:choose>
							<c:when test="${sezioniCollocazioniListaForm.richiamo eq 'lista'}">
								<td bgcolor="${color}" class="testoNormale"><sbn:linkbutton name="item"
									property="selectedSez" index="listaIdx" value="codSezione" title="Scegli Sezione"
									key="documentofisico.bottone.scegliSezione" bundle="documentoFisicoLabels"
									checkAttivita="df" /></td>
							</c:when>
							<c:otherwise>
								<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
									property="codSezione" /></td>
							</c:otherwise>
						</c:choose>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrTipoSez" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrTipoColl" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="inventariCollocati" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrSezione" /></td>
						<td bgcolor="${color}" class="testoNormale"><html:radio property="selectedSez"
							disabled="${sezioniCollocazioniListaForm.disable}" value="${listaIdx}" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		<div id="divFooterCommon"></div>
		</div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchiSez" parameter="methodSezColl" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td --> <c:choose>
			<c:when test="${sezioniCollocazioniListaForm.richiamo eq 'lista'}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodSezColl">
							<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
						</html:submit> <%-- <html:submit styleClass="pulsanti" property="methodSezColl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit>--%></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${sezioniCollocazioniListaForm.conferma}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodSezColl">
							<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodSezColl">
							<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table align="center">
					<tr>
						<td><sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti" disabled="false" property="methodSezColl">
								<bean:message key="documentofisico.bottone.nuova" bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti"
								disabled="${sezioniCollocazioniListaForm.noSezione}" property="methodSezColl">
								<bean:message key="documentofisico.bottone.modifica"
									bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita> <html:submit styleClass="pulsanti"
							disabled="${sezioniCollocazioniListaForm.noSezione}" property="methodSezColl">
							<bean:message key="documentofisico.bottone.esamina" bundle="documentoFisicoLabels" />
						</html:submit> <sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti"
								disabled="${sezioniCollocazioniListaForm.noSezione}" property="methodSezColl">
								<bean:message key="documentofisico.bottone.cancella"
									bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita> <%-- <html:submit styleClass="pulsanti" property="methodSezColl">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit>--%></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
