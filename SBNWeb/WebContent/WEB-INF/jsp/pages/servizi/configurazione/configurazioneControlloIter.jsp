<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform
		action="/servizi/configurazione/ConfigurazioneControlloIter.do">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
		</div>
		<br />
		<div>
		<div class="etichetta"
			style="float: left; width: 100px; font-weight: bold;"><bean:message
			key="servizi.utenti.biblioteca" bundle="serviziLabels" />&nbsp;&nbsp;
		</div>
		<div style="float: none;"><html:text styleId="testoNoBold"
			readonly="true" property="biblioteca" size="8"></html:text></div>
		</div>
		<br />
		<div>
		<div class="etichetta"
			style="float: left; width: 100px; font-weight: bold;"><bean:message
			key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />&nbsp;&nbsp;
		</div>
		<div style="float: none;"><html:text styleId="testoNoBold"
			readonly="true" property="tipoServizio.descrizione" size="50"></html:text>
		</div>
		</div>
		<br />
		<!-- Attività selezionata -->
		<div>
		<div class="etichetta"
			style="float: left; width: 100px; font-weight: bold;"><bean:message
			key="servizi.erogazione.attivita" bundle="serviziLabels" />&nbsp;&nbsp;
		</div>
		<div style="float: none;"><html:text styleId="testoNoBold"
			readonly="true" property="iterServizio.descrizione" size="50"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		</div>
		<br />
		<br />

		<div style="font-weight: bold;"><bean:message
			key="servizi.configurazione.controlloIter.elencoControlliAssociati"
			bundle="serviziLabels" /></div>
		<br />
		<table class="sintetica">
			<tr class="etichetta" bgcolor="#dde8f0">
				<th class="etichetta" scope="col" style="text-align: center;width: 5%;">
				<bean:message key="servizi.utenti.headerProgressivo"
					bundle="serviziLabels" /></th>
				<th class="etichetta" scope="col" style="text-align: center;">
				<bean:message key="servizi.label.descrizione" bundle="serviziLabels" />
				</th>
				<th class="etichetta" scope="col" style="text-align: center;width: 7%;">
				<bean:message key="servizi.label.bloccante" bundle="serviziLabels" />
				</th>
				<th scope="col" style="width: 4%; text-align: center;">&nbsp;</th>

				<%-- <th class="etichetta" scope="col"
					style="width: 4%; text-align: center;"><bean:message
					key="servizi.utenti.headerSelezionataMultipla"
					bundle="serviziLabels" /></th> --%>
			</tr>
			<c:choose>
				<c:when
					test="${not empty ConfigurazioneControlloIterForm.controlloIterMap}">
					<logic:iterate id="item" property="controlloIterMap"
						name="ConfigurazioneControlloIterForm" indexId="riga">
						<sbn:rowcolor var="color" index="riga" />
						<tr>

							<!-- <td bgcolor="${color}" class="testoNormale" style="text-align: center;"><bean-struts:write
								name="item" property="value.progrFase" /></td> -->

							<td bgcolor="${color}" class="testoNormale" style="text-align: center;">
								<sbn:linkbutton index="value.progrFase"
								name="item" value="value.progrFase" key="servizi.bottone.esamina"
								bundle="serviziLabels" title="esamina" property="codControlloScelto" />
							</td>

							<td bgcolor="${color}" class="testoNormale"><bean-struts:write
								name="item" property="value.descControllo" /></td>

							<td bgcolor="${color}" class="testoNormale" style="text-align: center;">
							<c:choose>
								<c:when test="${item.value.flagBloc}"><bean:message key="servizi.configurazione.controlli.bloccante.si"
								bundle="serviziLabels" />
								</c:when>
								<c:otherwise><bean:message key="servizi.configurazione.controlli.bloccante.no"
								bundle="serviziLabels" />
								</c:otherwise>
							</c:choose>
							</td>

							<td bgcolor="${color}" align="center"><html:radio
								property="codControlloScelto" value="${item.value.progrFase}"
								titleKey="servizi.configurazione.controlloIter.selezioneSingola"
								bundle="serviziLabels"></html:radio></td>
							<%-- <td bgcolor="${color}" align="center"><html:multibox
								property="lstCodControlloScelti"
								titleKey="servizi.configurazione.controlloIter.selezioneMultipla"
								bundle="serviziLabels" value="${item.value.codControllo}"></html:multibox>
							</td> --%>
						</tr>
					</logic:iterate>
				</c:when>
			</c:choose>
		</table>
		</div>
		<br />
		<br />

		<div id="divFooter" align="center">

		<c:choose>

			<c:when test="${ConfigurazioneControlloIterForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>

			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazioneControlloIter" titleKey="servizi.configurazione.servizio.nuovoControllo" bundle="serviziLabels">
					<bean:message key="servizi.bottone.aggiungiNuovo" bundle="serviziLabels" />
				</html:submit> &nbsp;
				</sbn:checkAttivita>
				<c:choose>

					<c:when
						test="${not empty ConfigurazioneControlloIterForm.controlloIterMap}">

						<html:submit property="methodConfigurazioneControlloIter" titleKey="servizi.configurazione.servizio.esaminaControllo" bundle="serviziLabels">
							<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
						</html:submit>
									&nbsp;
						<sbn:checkAttivita idControllo="GESTIONE">
						<html:submit property="methodConfigurazioneControlloIter" titleKey="servizi.configurazione.servizio.cancellaControllo" bundle="serviziLabels">
							<bean:message key="servizi.bottone.cancella"
								bundle="serviziLabels" />
						</html:submit>
									&nbsp;
						</sbn:checkAttivita>
						<%--
						<html:submit property="methodConfigurazioneControlloIter" titleKey="servizi.configurazione.servizio.inserisceControllo" bundle="serviziLabels">
							<bean:message key="servizi.bottone.inserisci"
								bundle="serviziLabels" />
						</html:submit>
									&nbsp;
						--%>
					</c:when>

				</c:choose>

				<html:submit property="methodConfigurazioneControlloIter" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
					<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
				</html:submit>

				<%--
				<c:choose>
					<c:when
						test="${not empty ConfigurazioneControlloIterForm.controlloIterMap}">
										&nbsp;&nbsp;&nbsp;&nbsp;
							<html:submit property="methodConfigurazioneControlloIter"
							styleClass="buttonFrecciaSu" titleKey="servizi.bottone.frecciaSu"
							bundle="serviziLabels">
							<bean:message key="servizi.bottone.frecciaSu"
								bundle="serviziLabels" />
							</html:submit>
							&nbsp;
							<html:submit property="methodConfigurazioneControlloIter"
							styleClass="buttonFrecciaGiu"
							titleKey="servizi.bottone.frecciaGiu" bundle="serviziLabels">
							<bean:message key="servizi.bottone.frecciaGiu"
								bundle="serviziLabels" />
							</html:submit>
					</c:when>
				</c:choose>
				--%>

			</c:otherwise>

		</c:choose></div>

	</sbn:navform>
</layout:page>
