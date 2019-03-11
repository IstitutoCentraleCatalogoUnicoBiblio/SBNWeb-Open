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
	<sbn:navform
		action="/documentofisico/modelliEtichette/modelliEtichetteLista.do">
		<div id="divForm"><html:hidden property="action" />
		<div id="divMessaggio"><sbn:errors /></div>
		<c:choose>
			<c:when test="${modelliEtichetteListaForm.richiamo eq 'lista'}">
				<table width="100%" border="0">
					<tr>
						<td colspan="3">
						<div class="etichetta"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /> <html:text
							disabled="true" styleId="testoNormale" property="codBib" size="5"
							maxlength="3"></html:text> <bean-struts:write
							name="modelliEtichetteListaForm" property="descrBib" /></div>
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table width="100%" border="0">
					<tr>
						<td colspan="4">
						<div class="etichetta"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /> <html:text
							disabled="true" styleId="testoNormale" property="codBib" size="5"
							maxlength="3"></html:text> <html:submit
							  title="Lista Modelli" styleClass="buttonImageListaSezione" disabled="false" property="methodModelli">
							<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
						</html:submit> <bean-struts:write name="modelliEtichetteListaForm" property="descrBib" /></div>
						</td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose> <br>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodModelli"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th class="etichetta" scope="col" width="10%"><bean:message
					key="documentofisico.prg" bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message
					key="documentofisico.modello" bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message
					key="documentofisico.descrizione" bundle="documentoFisicoLabels" /></th>
				<th scope="col" width="3%"></th>
			</tr>
			<logic:notEmpty property="listaModelli" name="modelliEtichetteListaForm">
				<logic:iterate id="item" property="listaModelli"
					name="modelliEtichetteListaForm" indexId="listaIdx">
					<sbn:rowcolor var="color" index="listaIdx" />
					<tr bgcolor="#FFCC99">
						<td bgcolor="${color}" class="testoNormale">
							<sbn:anchor name="item" property="prg" />
							<bean-struts:write name="item" property="prg" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write
							name="item" property="codModello" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write
							name="item" property="descrModello" /></td>
						<td bgcolor="${color}" class="testoNormale"><html:radio
							property="selectedModello" value="${listaIdx}" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		<div id="divFooterCommon"></div>
		</div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
			totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodModelli" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td --> <c:choose>
			<c:when test="${modelliEtichetteListaForm.richiamo eq 'lista'}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodModelli">
							<bean:message key="documentofisico.bottone.scegli"
								bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodModelli">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${modelliEtichetteListaForm.conferma}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodModelli">
							<bean:message key="documentofisico.bottone.si"
								bundle="documentoFisicoLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodModelli">
							<bean:message key="documentofisico.bottone.no"
								bundle="documentoFisicoLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table align="center">
					<tr>
						<td><sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti" disabled="false"
								property="methodModelli">
								<bean:message key="documentofisico.bottone.nuova"
									bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti"
								disabled="${modelliEtichetteListaForm.noModello}"
								property="methodModelli">
								<bean:message key="documentofisico.bottone.modifica"
									bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita> <html:submit styleClass="pulsanti"
							disabled="${modelliEtichetteListaForm.noModello}"
							property="methodModelli">
							<bean:message key="documentofisico.bottone.esamina"
								bundle="documentoFisicoLabels" />
						</html:submit> <sbn:checkAttivita idControllo="df">
							<html:submit styleClass="pulsanti"
								disabled="${modelliEtichetteListaForm.noModello}"
								property="methodModelli">
								<bean:message key="documentofisico.bottone.cancella"
									bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita> </td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
