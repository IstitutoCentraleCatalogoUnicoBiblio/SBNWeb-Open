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
		action="/gestionebibliografica/utility/dettaglioPropostaDiCorrezione.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" /></div>

				<table border="0">
					<tr>
						<td class="etichetta"><bean:message key="proposta.idProposta" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:text property="proposteDiCorrezioneView.idProposta" size="20" maxlength="80" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="proposta.idOggettoProposta" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale">
							<c:choose>
								<c:when test="${dettaglioPropostaDiCorrezioneForm.tipoProspettazione eq 'INS'}">
									<html:text property="proposteDiCorrezioneView.idOggetto" size="20" maxlength="80"></html:text>
								</c:when>
								<c:otherwise>
									<html:text property="proposteDiCorrezioneView.idOggetto" size="20" maxlength="80" readonly="true"></html:text>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
				<table border="0">
					<tr>
						<td class="etichetta"><bean:message key="proposta.proponente" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:text property="proposteDiCorrezioneView.mittenteBiblioteca" size="20" maxlength="20" readonly="true"></html:text></td>
						<td class="testoNormale"><html:text property="proposteDiCorrezioneView.mittenteUserId" size="20" maxlength="20"readonly="true"></html:text></td>
						<td class="etichetta"><bean:message key="proposta.dataInserimentoPropostaDa" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:text property="proposteDiCorrezioneView.dataInserimento" size="20" maxlength="80" readonly="true"></html:text></td>
						<td class="etichetta"><bean:message key="proposta.statoProposta" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:select property="proposteDiCorrezioneView.statoProposta" style="width:90px">
						<html:optionsCollection property="listaStatiProposta" value="codice" label="descrizioneCodice" /></html:select></td>
					</tr>
				</table>
				<table border="0">
					<tr>
						<td width="120" class="etichetta"><bean:message key="proposta.testoProposta" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale">
							<html:textarea property="proposteDiCorrezioneView.testo" cols="100" rows="3">
							</html:textarea>
						</td>
					</tr>
				</table>
				<table border="0">
				</table>

				<c:choose>
					<c:when test="${dettaglioPropostaDiCorrezioneForm.tipoProspettazione eq 'INS'}">
						<table border="0">
							<tr>
								<td width="120" class="etichetta"><bean:message key="proposta.destinatarioProposta" bundle="gestioneBibliograficaLabels" />:</td>
								<td class="testoNormale"><html:select property="proposteDiCorrezioneView.destinatariBiblio" style="width:90px">
										<html:optionsCollection property="listaBibliotecari" value="codice" label="descrizioneCodice" /></html:select>
								</td>
							</tr>
						</table>
					</c:when>
					<c:when test="${dettaglioPropostaDiCorrezioneForm.tipoProspettazione eq 'MOD'}">
						<hr color="#dde8f0" />

						<table width="100%" border="0">
							<tr bgcolor="#dde8f0">
								<th class="etichetta"><bean:message key="proposta.elencoRisposte"	bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta"></th>
								<th class="etichetta"></th>
							</tr>
							<logic:iterate id="item" property="listaDestinatariProp" name="dettaglioPropostaDiCorrezioneForm" indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale" bgcolor="#FEF1E2">
								<td bgcolor="${color}">
									<bean-struts:write name="item" property="destinatariBiblio" filter="false" />
								</td>
								<td bgcolor="${color}">
									<bean-struts:write name="item" property="destinatariData" />
								</td>
								<td bgcolor="${color}">
									<bean-struts:write name="item" property="destinatariNote" />
								</td>
							</tr>
							</logic:iterate>
						</table>

						<hr color="#dde8f0" />
						<table border="0">
							<tr>
								<td width="120" class="etichetta"><bean:message key="proposta.risposta" bundle="gestioneBibliograficaLabels" />:</td>
								<td class="testoNormale"><html:text property="proposteDiCorrezioneView.destinatariData" size="20" maxlength="20"  readonly="true"></html:text></td>
								<td class="testoNormale"><html:text property="proposteDiCorrezioneView.destinatariBiblio" size="20" maxlength="20" readonly="true"></html:text></td>
								<!--<td class="testoNormale"><html:text property="proposteDiCorrezioneView.destinatariNote" size="100" maxlength="100"></html:text></td>-->
								<td class="testoNormale"><html:textarea property="proposteDiCorrezioneView.destinatariNote" cols="80" rows="3"></html:textarea></td>

							</tr>
						</table>
					</c:when>
					<c:when test="${dettaglioPropostaDiCorrezioneForm.tipoProspettazione eq 'DET'}">
						<hr color="#dde8f0" />

						<table width="100%" border="0">
							<tr bgcolor="#dde8f0">
								<th class="etichetta"><bean:message key="proposta.elencoRisposte"	bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta"></th>
								<th class="etichetta"></th>
							</tr>
							<logic:iterate id="item" property="listaDestinatariProp" name="dettaglioPropostaDiCorrezioneForm" indexId="riga">
							<sbn:rowcolor var="color" index="riga" />
							<tr class="testoNormale" bgcolor="#FEF1E2">
								<td bgcolor="${color}">
									<bean-struts:write name="item" property="destinatariBiblio" filter="false" />
								</td>
								<td bgcolor="${color}">
									<bean-struts:write name="item" property="destinatariData" />
								</td>
								<td bgcolor="${color}">
									<bean-struts:write name="item" property="destinatariNote" />
								</td>
							</tr>
							</logic:iterate>
						</table>
					</c:when>

				</c:choose>
		</div>

		<div id="divFooter">
				<table align="center">
					<tr>
						<c:choose>
							<c:when test="${dettaglioPropostaDiCorrezioneForm.tipoProspettazione ne 'DET'}">
								<td align="center"><html:submit property="methodDettagPropCorrez">
									<bean:message key="button.caricaListeDest" bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
								<td align="center"><html:submit property="methodDettagPropCorrez">
									<bean:message key="button.confermaPropostaCorr" bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</c:when>
						</c:choose>
						<td align="center"><html:submit property="methodDettagPropCorrez">
							<bean:message key="button.annullaPropostaCorr" bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</tr>
				</table>
		</div>
	</sbn:navform>
</layout:page>
