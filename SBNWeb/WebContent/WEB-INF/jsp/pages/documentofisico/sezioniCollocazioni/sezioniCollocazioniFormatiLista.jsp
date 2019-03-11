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
		action="/documentofisico/sezioniCollocazioni/sezioniCollocazioniFormatiLista.do">
		<div id="divForm"><html:hidden property="action" />
		<div id="divMessaggio"><!--<div class="messaggioInfo">--><sbn:errors
			bundle="documentoFisicoMessages" /></div>
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text><bean-struts:write
					name="sezioniCollocazioniFormatiListaForm" property="descrBib" /></td>
				<c:choose>
					<c:when test="${sezioniCollocazioniFormatiListaForm.richiamo eq 'lista'}">
					</c:when>
					<c:otherwise>
						<td><bean:message key="documentofisico.sezioneT" bundle="documentoFisicoLabels" />
						<html:text disabled="true" styleId="testoNormale" property="codSez" size="15"
							maxlength="10"></html:text></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodSezCollFor"></sbn:blocchi>
		<table width="100%">
			<tr bgcolor="#dde8f0">
				<th>
				<div class="etichetta"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></div>
				</th>
				<c:choose>
					<c:when test="${sezioniCollocazioniFormatiListaForm.richiamo eq 'lista'}">
						<th class="etichetta"><bean:message key="documentofisico.sezione"
							bundle="documentoFisicoLabels" /></th>
					</c:when>
				</c:choose>
				<th class="etichetta"><bean:message key="documentofisico.formato"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.descrizione"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.numSerie"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.nAssegnato"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.numPezzi"
					bundle="documentoFisicoLabels" /></th>
				<%--<c:choose>
					<c:when test="${sezioniCollocazioniFormatiListaForm.sezione.tipoSezione eq 'L'}">--%>
						<th class="etichetta"><bean:message key="documentofisico.numeroPezziMisc"
							bundle="documentoFisicoLabels" /></th>
					<%--</c:when>
				</c:choose>--%>
				<c:choose>
					<c:when test="${sezioniCollocazioniFormatiListaForm.richiamo eq 'lista'}">
						<sbn:checkAttivita idControllo="df">
							<c:choose>
								<c:when test="${sezioniCollocazioniFormatiListaForm.esamina}">
								</c:when>
								<c:otherwise>
									<th class="etichetta"></th>
								</c:otherwise>
							</c:choose>
						</sbn:checkAttivita>
					</c:when>
					<c:otherwise>
						<th class="etichetta"></th>
					</c:otherwise>
				</c:choose>
			</tr>
			<logic:notEmpty property="listaFormatiSezione"
				name="sezioniCollocazioniFormatiListaForm">
				<logic:iterate id="item" property="listaFormatiSezione"
					name="sezioniCollocazioniFormatiListaForm" indexId="listaIdx">
					<sbn:rowcolor var="color" index="listaIdx" />
					<tr bgcolor="#FFCC99">
						<td bgcolor="${color}">
						<sbn:anchor name="item" property="prg" />
						<div align="center" class="testoNormale"><bean-struts:write name="item"
							property="prg" /></div>
						</td>
						<c:choose>
							<c:when test="${sezioniCollocazioniFormatiListaForm.richiamo eq 'lista'}">
								<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
									property="codSez" /></td>
							</c:when>
						</c:choose>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="codFormato" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrFor" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="progSerie" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="progNum" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="numeroPezzi" /></td>
						<%--<c:choose>
							<c:when test="${sezioniCollocazioniFormatiListaForm.sezione.tipoSezione eq 'L'}">--%>
								<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
									property="numeroPezziMisc" /></td>
							<%--</c:when>
						</c:choose>--%>
						<c:choose>
							<c:when test="${sezioniCollocazioniFormatiListaForm.richiamo eq 'lista'}">
								<sbn:checkAttivita idControllo="df">
									<c:choose>
										<c:when test="${sezioniCollocazioniFormatiListaForm.esamina}">
										</c:when>
										<c:otherwise>
											<td bgcolor="${color}" class="testoNormale"><html:radio
												disabled="${sezioniCollocazioniFormatiListaForm.disable}"
												property="selectedFor" value="${listaIdx}" /></td>
										</c:otherwise>
									</c:choose>
								</sbn:checkAttivita>
							</c:when>
							<c:otherwise>
								<td bgcolor="${color}" class="testoNormale"><html:radio
									disabled="${sezioniCollocazioniFormatiListaForm.disable}" property="selectedFor"
									value="${listaIdx}" /></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		</div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodSezCollFor" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<td><c:choose>
					<c:when test="${sezioniCollocazioniFormatiListaForm.richiamo eq 'lista'}">
						<sbn:checkAttivita idControllo="df">
							<c:choose>
								<c:when test="${sezioniCollocazioniFormatiListaForm.esamina}">
								</c:when>
								<c:otherwise>
									<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
										<bean:message key="documentofisico.bottone.scegli"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</c:otherwise>
							</c:choose>
						</sbn:checkAttivita>
						<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${sezioniCollocazioniFormatiListaForm.esamina}">
								<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
									<bean:message key="documentofisico.bottone.esamina"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<sbn:checkAttivita idControllo="df">
									<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
										<bean:message key="documentofisico.bottone.nuovoFormato"
											bundle="documentoFisicoLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
										<bean:message key="documentofisico.bottone.modifica"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</sbn:checkAttivita>
								<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
									<bean:message key="documentofisico.bottone.esamina"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" disabled="false" property="methodSezCollFor">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
