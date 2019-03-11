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
	<sbn:navform action="/documentofisico/codiciProvenienza/codiciProvenienzaLista.do">
		<html:hidden property="action" />
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<c:choose>
			<c:when test="${codiciProvenienzaListaForm.richiamo eq 'lista'}">
				<table width="100%" border="0">
					<tr>
						<td colspan="3">
						<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
							bundle="documentoFisicoLabels" /> <html:text disabled="true"
							styleId="testoNormale" property="codBib" size="5" maxlength="3"></html:text> <bean-struts:write
							name="codiciProvenienzaListaForm" property="descrBib" /></div>
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
							disabled="true"> <html:submit disabled="false" title="Lista Provenienze"
							styleClass="buttonImageListaSezione" property="methodProvInvLista">
							<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
						</html:submit></span> <bean-struts:write name="codiciProvenienzaListaForm" property="descrBib" /></div>
						</td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose> <sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodProvInvLista"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th class="etichetta" scope="col"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.codice"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.descrizione"
					bundle="documentoFisicoLabels" /></th>
				<th width="4%" class="etichetta" scope="col">&nbsp;</th>
			</tr>
			<logic:notEmpty property="listaProvInv" name="codiciProvenienzaListaForm">
				<logic:iterate id="item" property="listaProvInv" name="codiciProvenienzaListaForm"
					indexId="listaIdx">
					<sbn:rowcolor var="color" index="listaIdx" />
					<tr bgcolor="#FFCC99">
						<td bgcolor="${color}" class="testoNormale">
							<sbn:anchor name="item" property="prg" />
							<bean-struts:write name="item"
								property="prg" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="codProvenienza" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrProvenienza" /></td>
						<td bgcolor="${color}" class="testoNormale"><html:radio property="selectedCod"
							value="${listaIdx}" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		</div>
		<div id="divFooterCommon"></div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodProvInvLista" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td --> <c:choose>
			<c:when test="${codiciProvenienzaListaForm.richiamo eq 'lista'}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodProvInvLista">
							<bean:message key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodProvInvLista">
							<bean:message key="documentofisico.bottone.chiudiP" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table align="center">
					<tr>
						<td><sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti" disabled="false" property="methodProvInvLista">
								<bean:message key="documentofisico.bottone.nuova" bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" disabled="false" property="methodProvInvLista">
								<bean:message key="documentofisico.bottone.modifica"
									bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita> <%--<html:submit styleClass="pulsanti" property="methodProvInvLista">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>--%></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
