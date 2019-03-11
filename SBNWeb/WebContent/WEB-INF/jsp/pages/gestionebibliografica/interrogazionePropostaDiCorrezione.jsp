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


<%@ page import="it.iccu.sbn.web.actionforms.gestionebibliografica.InterrogazionePropostaDiCorrezioneForm"%>


<html:xhtml/>
<layout:page>
	<sbn:navform
		action="/gestionebibliografica/utility/interrogazionePropostaDiCorrezione.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" /></div>

			<table border="0">
				<tr>
					<td width="150" class="etichetta"><bean:message key="proposta.idProposta" bundle="gestioneBibliograficaLabels" />:</td>
					<td class="testoNormale"><html:text property="areaDatiPropostaDiCorrezioneVO.idProposta" size="20" maxlength="80"></html:text></td>
				</tr>
				<tr>
					<td class="etichetta"><bean:message key="proposta.idOggettoProposta" bundle="gestioneBibliograficaLabels" />:</td>
					<td class="testoNormale"><html:text property="areaDatiPropostaDiCorrezioneVO.idOggettoProposta" size="20" maxlength="80"></html:text></td>

				</tr>
			</table>


	<!-- Modifica almaviva2 BUG 3900 26.10.2010 i check richiestiDaMe e richiestiAMe vengono trasformati in Radio Button esclusivi;-->
			<table border="0">
				<tr>
					<td>
						<bean:message key="proposta.richDaMe" bundle="gestioneBibliograficaLabels" />
						<html:radio	property="tipoRichiesta" value="richDaMe"/>
						<bean:message key="proposta.richAMe" bundle="gestioneBibliograficaLabels" />
						<html:radio	property="tipoRichiesta" value="richAMe"/>
					</td>
				</tr>
			</table>

			<table border="0">
				<tr>
					<td class="etichetta"><bean:message key="proposta.dataInserimentoPropostaDa" bundle="gestioneBibliograficaLabels" />:</td>
					<td class="testoNormale"><html:text property="areaDatiPropostaDiCorrezioneVO.dataInserimentoPropostaDa" size="15" maxlength="10"></html:text></td>
					<td class="etichetta"><bean:message key="proposta.dataInserimentoPropostaA" bundle="gestioneBibliograficaLabels" />:</td>
					<td class="testoNormale"><html:text property="areaDatiPropostaDiCorrezioneVO.dataInserimentoPropostaA" size="15" maxlength="10"></html:text></td>
				</tr>
			</table>

			<!--<table border="0">
				<tr>
					<td class="etichetta"><bean:message key="proposta.statoProposta" bundle="gestioneBibliograficaLabels" />:</td>
					<td class="testoNormale"><html:text property="areaDatiPropostaDiCorrezioneVO.statoProposta" size="20" maxlength="80"></html:text></td>
					<td class="testoNormale"><html:select property="areaDatiPropostaDiCorrezioneVO.statoProposta" style="width:90px">
						<html:optionsCollection property="listaStatiProposta" value="codice" label="descrizioneCodice" /></html:select></td>
				</tr>
			</table> -->

		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center"><html:submit property="methodInterrogPropCorrez">
						<bean:message key="button.cercaPropostaCorr" bundle="gestioneBibliograficaLabels" />
					</html:submit></td>
					<c:choose>
						<c:when test="${interrogazionePropostaDiCorrezioneForm.tastoCreaPresente eq 'SI'}">
							<td align="center"><html:submit property="methodInterrogPropCorrez">
								<bean:message key="button.creaPropostaCorr" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
					</c:choose>
				</tr>
			</table>

		</div>
	</sbn:navform>
</layout:page>

