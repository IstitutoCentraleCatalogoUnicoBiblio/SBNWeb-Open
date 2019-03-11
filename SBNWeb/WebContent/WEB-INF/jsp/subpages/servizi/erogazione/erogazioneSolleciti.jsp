<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br>

<c:if test="${not empty navForm.listaSolleciti}">
		<span class="etichetta" style="font-weight: bolder;" >
			<bean:message key="servizi.erogazione.listaSolleciti" bundle="serviziLabels" />
		</span>
		<hr/>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
			elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
			parameter="methodErogazione" />

		<table class="sintetica">
			<tr>
				<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
				</th>
				<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.movimento.dataScadenza" bundle="serviziLabels" />
				</th>
				<th width="5%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.numSolleciti" bundle="serviziLabels" />
				</th>
				<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
				</th>
				<th width="30%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />
				</th>
				<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.listaMovimenti.documentoCollocazione" bundle="serviziLabels" />
				</th>
				<%--
				<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.statoRichiesta" bundle="serviziLabels" />
				</th>
				--%>

				<th width="12%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
				</th>

				<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.erogazione.dataUltimoSoll" bundle="serviziLabels" />
				</th>
				<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
				</th>
				<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
					<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
				</th>
			</tr>

			<logic:iterate id="listaMov" property="listaSolleciti" name="navForm" indexId="progr">
				<sbn:rowcolor var="color" index="progr" />

				<tr>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
						<sbn:anchor name="listaMov" property="progr"/> <sbn:linkbutton
							name="listaMov" property="codSolSing" index="idRichiesta"
							value="idRichiesta" key="servizi.bottone.esamina"
							bundle="serviziLabels" disabled="${navForm.conferma}"/>
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="dataFinePrevNoOraString" />
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="numSolleciti" />
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="cognomeNome"/>
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
						<bean-struts:write name="listaMov" property="titolo" />
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="datiDocumento" />
					</td>

					<%--
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="stato_richiesta" />
					</td>
					--%>

					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="tipoServizio" />
					</td>

					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<bean-struts:write name="listaMov" property="dataUltimoSollecito" />
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<html:radio property="codSolSing" value="${listaMov.idRichiesta}" style="text-align:center;"
									titleKey="servizi.erogazione.listaSolleciti.selezioneSingola" bundle="serviziLabels"
									disabled="${navForm.conferma}" />
					</td>
					<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
						<html:multibox  property="codSolMul" value="${listaMov.idRichiesta}" style="text-align:center;"
										titleKey="servizi.erogazione.listaSolleciti.selezioneMultipla" bundle="serviziLabels"
										disabled="${navForm.conferma}" />
					</td>
				</tr>
			</logic:iterate>
		</table>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
			elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
			parameter="methodErogazione" bottom="true"/>

		<div id="divFooterCommon">
			<table border="0">
				<tr>
					<td class="etichetta">
						<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
					</td>
					<td  class="testoNormale">
						<html:select property="ordSolleciti" disabled="${navForm.conferma}" >
							<html:optionsCollection property="ordinamentiSolleciti" value="codice" label="descrizione" />
						</html:select>
					</td>
					<td>&nbsp;</td>
					<td class="etichetta">
						<bean:message key="servizi.erogazione.solleciti.escludiSollecitiProrogati" bundle="serviziLabels" />
					</td>
					<td  class="testoNormale">
						<html:checkbox property="anaMov.escludiSollecitiProrogati" value="true" disabled="${navForm.conferma}" />
						<html:hidden property="anaMov.escludiSollecitiProrogati" value="false"/>
					</td>
				</tr>
			</table>
		</div>
</c:if>