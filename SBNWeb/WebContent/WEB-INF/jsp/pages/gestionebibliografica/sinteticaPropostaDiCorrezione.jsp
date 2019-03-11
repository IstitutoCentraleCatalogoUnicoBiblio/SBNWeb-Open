<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
		almaviva2 - Inizio Codifica Agosto 2006
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml/>
<layout:page>
	<sbn:navform
		action="/gestionebibliografica/utility/sinteticaPropostaDiCorrezione.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" /></div>

				<table width="100%" border="0">
					<tr bgcolor="#dde8f0">
						<th class="etichetta"><bean:message key="proposta.idProposta"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="proposta.statoProposta"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.data"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="proposta.mittenteProposta"
							bundle="gestioneBibliograficaLabels" /></th>
						<!--<th class="etichetta"><bean:message key="proposta.destinatarioProposta"
							bundle="gestioneBibliograficaLabels" /></th>-->
						<th class="etichetta"><bean:message key="proposta.idOggettoProposta"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="proposta.testoProposta"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"></th>
					</tr>

					<logic:iterate id="item" property="listaSintetica"	name="sinteticaPropostaDiCorrezioneForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr class="testoNormale" bgcolor="#FEF1E2">
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="idProposta" filter="false" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="statoProposta" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="dataInserimento" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="mittenteBiblioteca" />
							-
							<bean-struts:write name="item" property="mittenteUserId" />
						</td>
						<!--<td bgcolor="${color}">
							<bean-struts:write name="item" property="destinatari" />
						</td>-->

						<td bgcolor="${color}">
							<bean-struts:write name="item" property="idOggetto" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="testo" />
						</td>
						<td bgcolor="${color}">
							<html:radio property="selezRadio" value="${item.idProposta}" />
						</td>

					</tr>
					</logic:iterate>
				</table>
		</div>

		<div id="divFooter">
				<table align="center">
					<tr>
						<c:choose>
							<c:when test="${sinteticaPropostaDiCorrezioneForm.presenzaTastoInsRisposta eq 'SI'}">
								<td align="center"><html:submit property="methodSintPropCorrez">
									<bean:message key="button.variaPropostaCorr" bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</c:when>
						</c:choose>

						<td align="center"><html:submit property="methodSintPropCorrez">
							<bean:message key="button.dettaglio" bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
						<c:choose>
							<c:when test="${sinteticaPropostaDiCorrezioneForm.tipoProspettazione eq 'SINcreaOK'}">
								<td align="center"><html:submit property="methodSintPropCorrez">
									<bean:message key="button.creaPropostaCorr" bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</c:when>
						</c:choose>

					</tr>
				</table>
		</div>
	</sbn:navform>
</layout:page>
