<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<table class="sintetica">
	<tr>
		<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.codRichILL" bundle="serviziLabels" />
		</th>
		<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
		</th>
		<th width="12%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
		</th>
		<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.movimento.dataScadenza" bundle="serviziLabels" />
		</th>
		<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
		</th>
		<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.ill.bib.fornitrice" bundle="serviziLabels" />
		</th>
		<th width="30%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />
		</th>
		<%--
		<th width="10%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.statoRichiesta" bundle="serviziLabels" />
		</th>
		<th width="10%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.ill.statoRichiestaLoc" bundle="serviziLabels" />
		</th>
		--%>
		<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.erogazione.movimento.attivitaAttuale" bundle="serviziLabels" />
		</th>
		<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
		</th>
		<%--
		<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
			<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
		</th>
		--%>
	</tr>

	<logic:iterate id="item" property="richieste" name="navForm" indexId="progr">
		<sbn:rowcolor var="color" index="progr" />

		<tr>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
				<sbn:anchor name="item" property="progr"/> <sbn:linkbutton
					name="item" property="selected" index="idRichiestaILL"
					value="transactionId" key="servizi.bottone.esamina"
					bundle="serviziLabels" disabled="${navForm.conferma}"/>
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
				<sbn:anchor name="item" property="progr"/> <sbn:linkbutton
					name="item" property="selected" index="cod_rich_serv"
					value="cod_rich_serv" key="servizi.bottone.esaminaLoc"
					bundle="serviziLabels" disabled="${navForm.conferma or item.cod_rich_serv eq 0}"/>
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
				<bs:write name="item" property="descrizioneServizio" />
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
				<bs:write name="item" property="dataScadenza" format="dd/MM/yyyy" />
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
				<bs:write name="item" property="cognomeNome"/>
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
				<span title="${item.denominazioneBibFornitrice}">
					<bs:write name="item" property="responderId" />
				</span>
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
				<bs:write name="item" property="titolo" />
			</td>
			<%--
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
				<bs:write name="item" property="descrizioneStatoRichiesta" />
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
				<bs:write name="item" property="descrizioneStatoRichiestaLoc" />
			</td>
			--%>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
				<bs:write name="item" property="ultimaAttivita" />
			</td>
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
				<html:radio property="selected" value="${item.repeatableId}" style="text-align:center;"
							titleKey="servizi.erogazione.listaSolleciti.selezioneSingola" bundle="serviziLabels"
							disabled="${navForm.conferma}" />
			</td>
			<%--
			<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
				<html:multibox  property="selectedItems" value="${item.repeatableId}" style="text-align:center;"
								titleKey="servizi.erogazione.listaSolleciti.selezioneMultipla" bundle="serviziLabels"
								disabled="${navForm.conferma}" />
			</td>
			--%>
		</tr>
	</logic:iterate>
</table>
