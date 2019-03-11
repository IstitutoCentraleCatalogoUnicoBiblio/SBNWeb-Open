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
		action="/documentofisico/esameCollocazioni/esameCollocRicercaEsemplare.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="documentoFisicoMessages" /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" width="18%"><bean:message
					key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />:</td>
				<td width="19%"><html:text disabled="true" styleId="testoNormale"
					property="codBib" size="10" maxlength="10"></html:text></td>
				<td width="56%" align="left"><span class="testoNormale"> <bean-struts:write
					name="esameCollocRicercaEsemplareForm" property="descrBib" /></span></td>
			</tr>
		</table>

		<table width="100%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
			<tr valign="top">
				<td class="etichetta"><bean:message
					key="documentofisico.notiziaCorrT" bundle="documentoFisicoLabels" />:
				<span class="etichetta"></span> <bean-struts:write
					name="esameCollocRicercaEsemplareForm" property="bid" /><bean-struts:write
					name="esameCollocRicercaEsemplareForm" property="titolo" />
			</tr>
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.bidCollocazioneT" bundle="documentoFisicoLabels" />:
				<bean-struts:write name="esameCollocRicercaEsemplareForm"
					property="recCollDett.bid" /><bean-struts:write name="esameCollocRicercaEsemplareForm" property="recCollDett.bidDescr" /></div>
				</td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<th width="19%" height="24" scope="col">
				<div align="left" class="etichetta">
				<div align="left"><bean:message key="documentofisico.sezioneT"
					bundle="documentoFisicoLabels" /></div>
				</div>
				</th>
				<td width="10%" scope="col">
				<div align="left" ><bean-struts:write
					name="esameCollocRicercaEsemplareForm" property="codSez" /></div>
				</td>
				<th width="17%" scope="col">
				<div align="left" class="testo">
				<div align="right"><bean:message key="documentofisico.collocazioneT"
					bundle="documentoFisicoLabels" /></div>
				</div>
				</th>
				<td width="18%" scope="col">
				<div align="left" ><bean-struts:write
					name="esameCollocRicercaEsemplareForm" property="codCollocazione" /></div>
				</td>
				<th width="20%" class="testo" scope="col">
				<div align="right"><bean:message
					key="documentofisico.specificazioneT" bundle="documentoFisicoLabels" /></div>
				</th>
				<td width="16%" scope="col">
				<div align="center" ><bean-struts:write
					name="esameCollocRicercaEsemplareForm" property="codSpecificazione" /></div>
				</td>
			</tr>
		</table>
		<!--
		folders
 -->
		<table width="100%" border="0">
			<tr>
				<c:choose>
					<c:when test="${esameCollocRicercaEsemplareForm.folder eq 'tab1'}">
						<td width="27%" class="schedaOn">
						<div align="center">Lista Esemplari del Reticolo</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="27%" class="schedaOff">
						<div align="center"><c:choose>
							<c:when test="${esameCollocRicercaEsemplareForm.tab1==true}">
								<html:submit property="methodEsameCollRicEsempl"
									styleClass="sintButtonLinkDefault">
									<bean:message key="documentofisico.esameEsemplRicercaTab1"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit property="methodEsameCollRicEsempl"
									styleClass="sintButtonLinkDefault">
									<bean:message key="documentofisico.esameEsemplRicercaTab1"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${esameCollocRicercaEsemplareForm.folder eq 'tab2'}">
						<td width="39%" class="schedaOn">
						<div align="center">Nuovo Esemplare</div>
					</c:when>
					<c:otherwise>
						<td width="39%" class="schedaOff">
						<div align="center"><html:submit
							property="methodEsameCollRicEsempl"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.esameEsemplRicercaTab2"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<!--
		richiamo subpages
--> <c:choose>
			<c:when test="${esameCollocRicercaEsemplareForm.folder eq 'tab1'}">
				<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
					totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsameCollRicEsempl"></sbn:blocchi>
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/esameCollocazioni/esameCollocRicercaEsemplareTab1.jsp" />
				<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe"
					totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsameCollRicEsempl" bottom="true"></sbn:blocchi>
			</c:when>
			<c:when test="${esameCollocRicercaEsemplareForm.folder eq 'tab2'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/esameCollocazioni/esameCollocRicercaEsemplareTab2.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><c:choose>
					<c:when test="${esameCollocRicercaEsemplareForm.folder eq 'tab1'}">
						<sbn:checkAttivita idControllo="df"><html:submit styleClass="pulsanti"
							property="methodEsameCollRicEsempl">
							<bean:message key="documentofisico.bottone.sceltaEsempl"
								bundle="documentoFisicoLabels" />
						</html:submit></sbn:checkAttivita>
						<html:submit styleClass="pulsanti"
							property="methodEsameCollRicEsempl">
							<bean:message key="documentofisico.bottone.esColl"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>

					<c:when test="${esameCollocRicercaEsemplareForm.folder eq 'tab2'}">
						<html:submit styleClass="pulsanti" disabled="false"
							property="methodEsameCollRicEsempl">
							<bean:message key="documentofisico.bottone.sceltaTit"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>

					<c:otherwise>
					</c:otherwise>
				</c:choose><!--

					<html:submit styleClass="pulsanti" property="methodEsameCollRicEsempl">
						<bean:message key="documentofisico.bottone.ok"
								bundle="documentoFisicoLabels" /></html:submit>
					--><html:submit styleClass="pulsanti"
					property="methodEsameCollRicEsempl">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>
