<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<br>
<span class="header" style="font-weight: bolder;" >
	<bean:message key="servizi.erogazione.listaPrenotazioni" bundle="serviziLabels" />
</span>
<hr/>

<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
	elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
	parameter="methodErogazione" />

<c:if test="${not empty ErogazioneRicercaForm.listaPrenotazioni}">

	<table class="sintetica">
		<tr>
			<th width="3%" class="header" scope="col">
				<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
			</th>
			<th width="7%" class="header" scope="col">
				<bean:message key="servizi.erogazione.movimento.dataRichiesta" bundle="serviziLabels" />
			</th>
			<th width="20%" class="header" scope="col">
				<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
			</th>
			<th width="30%" class="header" scope="col">
				<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />
			</th>
			<th width="7%" class="header" scope="col">
				<bean:message key="servizi.erogazione.listaMovimenti.documentoCollocazione" bundle="serviziLabels" />
			</th>
			<%--
			<th width="20%" class="header" scope="col">
				<bean:message key="servizi.erogazione.statoRichiesta" bundle="serviziLabels" />
			</th>
			--%>
			<th width="7%" class="header" scope="col">
				<bean:message key="servizi.erogazione.dataInizioPrevista" bundle="serviziLabels" />
			</th>
			<th width="12%" class="header" scope="col">
				<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
			</th>
			<th width="7%" class="header" scope="col">
				<bean:message key="servizi.erogazione.dataMassima" bundle="serviziLabels" />
			</th>
			<th width="3%" class="header" scope="col">
				<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
			</th>
			<%--<th width="3%" class="header" scope="col">
				<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
			</th> --%>
		</tr>

		<logic:iterate id="listaMov" property="listaPrenotazioni" name="ErogazioneRicercaForm" indexId="progr">
			<tr class="row alt-color">
				<td>
					<sbn:anchor name="listaMov" property="progr"/> <sbn:linkbutton
						name="listaMov" property="codPreSing" index="codRichServ"
						value="codRichServ" key="servizi.bottone.esamina"
						bundle="serviziLabels" withAnchor="false" />
				</td>
				<td>
					<bean-struts:write name="listaMov" property="dataRichiestaNoOraString" />
				</td>
				<td>
					<bean-struts:write name="listaMov" property="cognomeNome"/>
				</td>
				<td>
					<bean-struts:write name="listaMov" property="titolo" />
				</td>
				<td>
					<bean-struts:write name="listaMov" property="datiDocumento" />
				</td>
				<%--
				<td>
					<bean-struts:write name="listaMov" property="stato_richiesta" />
				</td>
				--%>
				<td>
					<bean-struts:write name="listaMov" property="dataInizioPrevNoOraString" />
				</td>
				<td>
					<bean-struts:write name="listaMov" property="tipoServizio" />
				</td>
				<td>
					<bean-struts:write name="listaMov" property="dataMaxString" />
				</td>
				<td>
					<bean-struts:define id="codval">
						<bean-struts:write name="listaMov" property="codRichServ" />
					</bean-struts:define>
					<html:radio property="codPreSing" value="${codval}" style="text-align:center;"
								titleKey="servizi.erogazione.listaPrenotazioni.selezioneSingola" bundle="serviziLabels" />
				</td>
				<%--<td>
					<html:multibox  property="codPreMul" value="${codval}" style="text-align:center;"
									titleKey="servizi.erogazione.listaPrenotazioni.selezioneMultipla" bundle="serviziLabels" />
				</td> --%>
			</tr>
		</logic:iterate>
	</table>

	<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
		elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
		parameter="methodErogazione" bottom="true"/>
</c:if>
<br/>
<table>
	<tr>
		<td>
			<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
		</td>
		<td>
			<html:select property="filtroPrenot.codTipoServ">
				<html:optionsCollection property="listaTipiServizio"
					value="codiceTipoServizio" label="descrizione" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td style="width: 6em;"><bean:message key="servizi.erogazione.movimento.dataInizioPrevista"
			bundle="serviziLabels" />
		<td>
			<bean:message key="servizi.documenti.dataDa" bundle="serviziLabels" />&nbsp;
			<html:text property="filtroPrenot.dataInizioPrevNoOraString" size="10" maxlength="10" />
		</td>
	</tr>
	<tr>
		<td class="etichetta">
			<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
		</td>
		<td  class="testoNormale">
			<html:select property="ordPrenotazioni">
				<html:optionsCollection property="ordinamentiPrenotazioni" value="codice" label="descrizione" />
			</html:select>
		</td>
	</tr>
</table>

<div id="divFooterCommon">

</div>
