<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/erogazione/InserimentoRichiesta.do">

		<div id="divForm">
			<div id="divMessaggio"> <sbn:errors bundle="serviziMessages" /></div>
			<br>
		</div>

		<tr>
		    <td class="etichetta" align="left">
		    <hr>
				<bean:message key="servizi.erogazione.inserimentoRichiesta.richiestaServizi"  bundle="serviziLabels" />:
				<bean:message key="servizi.erogazione.inserimentoRichiesta.autoreLogin"  bundle="serviziLabels" />
				<em><strong><c:out value="${InserimentoRichiestaForm.autore}"> </c:out></strong></em> ,
				<bean:message key="servizi.erogazione.inserimentoRichiesta.titoloLogin"  bundle="serviziLabels" />
				<em><strong><c:out value="${InserimentoRichiestaForm.titolo}"> </c:out></strong></em> del
				<em><strong><c:out value="${InserimentoRichiestaForm.anno}"> </c:out></strong></em>
				<hr>
			</td>
		</tr>

		<table style="margin-top:0"  border="1" >
			<tr>
				<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.servizio.richiesto"  bundle="serviziLabels" /></strong></em>:<c:out value="${InserimentoRichiestaForm.servizio}"> </c:out></td>
			    <td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.dataRic"  bundle="serviziLabels" /></strong></em><c:out value="${InserimentoRichiestaForm.dataRic}"> </c:out></td>
			</tr>

			<c:if test="${InserimentoRichiestaForm.tariffa>0}">
					<tr>
					  	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.tariffa"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="tariffa" disabled="true"></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
			</c:if>
			<c:if test="${not empty InserimentoRichiestaForm.mostraCampi}">
			<logic:iterate id="item" name="InserimentoRichiestaForm" property="mostraCampi">

				<c:if test="${item.campoRichiesta eq 9}">
					<tr>
					  	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.volInter"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="volInter" maxlength="4"></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 10}">
					<tr>
					 	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.numFasc"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="numFasc" maxlength="30"></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 11}">
					<tr>
					 	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.intCopia"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="intcopia" maxlength="30"></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 12}">
					<tr>
					  	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.noteUte"  bundle="serviziLabels" /></strong></em></td>
					  	<td><html:textarea name="InserimentoRichiestaForm" property="noteUte" rows="6" cols="42" ></html:textarea>
					  	<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 13}">
					<tr>
						<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.spesa"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="spesaMax" maxlength="12"></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 15}">
					<tr>
						<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.dataLim"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="dataLimInteresse" maxlength="10"></html:text>(gg/mm/aaaa)
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 16}">
					<tr>
						<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.dataPrevistaRitiroDocumento"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="dataPrevRitiroDocumento" maxlength="10"></html:text>(gg/mm/aaaa)
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 18}">
					<tr>
						<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.dataDisponibDocumento"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="dataDisponibDocumento" maxlength="10"></html:text>(gg/mm/aaaa)
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 22}">
					<tr>
						<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.modErog"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="listamodalitaErogazione" ></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 24}">
					<tr>
						<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.supporto"  bundle="serviziLabels" /></strong></em></td>
						<td><html:text name="InserimentoRichiestaForm" property="listaSupporti" ></html:text>
						<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 25}">
					<tr>
					 	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.sala"  bundle="serviziLabels" /></strong></em></td>
					 	<td><html:text name="InserimentoRichiestaForm" property="sala" maxlength="4"></html:text>
					 	<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 26}">
					<tr>
					 	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.posto"  bundle="serviziLabels" /></strong></em></td>
					 	<td><html:text name="InserimentoRichiestaForm" property="posto" maxlength="4"></html:text>
					 	<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

				<c:if test="${item.campoRichiesta eq 99}">
					<tr>
					 	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.annoPeriodico"  bundle="serviziLabels" /></strong></em></td>
					 	<td><html:text name="InserimentoRichiestaForm" property="annoPeriodico" maxlength="4"></html:text>
					 	<c:if test="${item.obbligatorio}">(*)</c:if></td>
					</tr>
				</c:if>

			</logic:iterate>
			</c:if>
		</table>

		<tr>
			<td class="etichetta" align="left">
				<hr><bean:message key="servizi.erogazione.inserimentoRichiesta.dtiObbligatori"  bundle="serviziLabels" /><hr>
			</td>
		</tr>

		<html:submit styleClass="submit" property="paramRichiestaServizio">
			<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
		</html:submit>

		<c:if test="${InserimentoRichiestaForm.tariffa eq 0}">
			<html:submit styleClass="submit" property="paramRichiestaServizio">
				<bean:message key="servizi.bottone.avanti" bundle="serviziLabels" />
			</html:submit>
		</c:if>

		<c:if test="${InserimentoRichiestaForm.tariffa >0}">
			<html:submit styleClass="submit" property="paramRichiestaServizio">
				<bean:message key="servizi.bottone.insRich" bundle="serviziLabels" />
			</html:submit>
		</c:if>

	</sbn:navform>
</layout:page>
