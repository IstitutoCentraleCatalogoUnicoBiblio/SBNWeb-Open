<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/comunicazioni/inserisciCom.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

			<logic:equal name="navForm"
				property="datiComunicazione.tipoDocumento" value="O">
				<bs:define id="noinputFatt" value="true" />
				<bs:define id="noinputOrd" value="false" />
			</logic:equal>
			<logic:equal name="navForm"
				property="datiComunicazione.tipoDocumento" value="F">
				<bs:define id="noinputFatt" value="false" />
				<bs:define id="noinputOrd" value="true" />
			</logic:equal>

			<logic:equal name="navForm"
				property="datiComunicazione.tipoDocumento" value="">
				<bs:define id="noinputFatt" value="true" />
				<bs:define id="noinputOrd" value="true" />
			</logic:equal>

			<bs:define id="noinput" value="false" />
			<c:choose>
				<c:when test="${navForm.disabilitaTutto}">
					<bs:define id="noinput" value="true" />
					<bs:define id="noinputFatt" value="true" />
					<bs:define id="noinputOrd" value="true" />
				</c:when>
			</c:choose>

			<table width="100%" border="0">
			<tr>
				<td colspan="6" class="etichetta">&nbsp;</td>
			</tr>
			<tr>
				<td class="etichetta" width="15%" scope="col" align="left"><bean:message
					key="ricerca.label.codBibl" bundle="acquisizioniLabels" /></td>
				<td scope="col" align="left"><html:text styleId="testoNormale"
					property="datiComunicazione.codBibl" size="4" readonly="true"></html:text>
				<html:submit title="elenco" styleClass="buttonImageListaSezione"
					property="methodinserisciCom" disabled="${noinput}">
					<bean:message key="ricerca.label.bibliolist"
						bundle="acquisizioniLabels" />
				</html:submit></td>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="ricerca.label.codMsg" bundle="acquisizioniLabels" /></td>
				<td scope="col" align="left"><html:text styleId="testoNormale"
					property="datiComunicazione.codiceMessaggio" size="5"
					readonly="true"></html:text></td>
				<td class="etichetta" width="15%" scope="col" align="left">
				Stato</td>
				<td scope="col" align="left"><html:select
					styleClass="testoNormale"
					property="datiComunicazione.statoComunicazione" disabled="true">
					<html:optionsCollection property="listaStatoComunicazione"
						value="codice" label="descrizione" />
				</html:select></td>

			</tr>
			<tr>
				<sbn:disableAll checkAttivita="SIF_DA_PERIODICO" inverted="true" >
					<td class="etichetta" scope="col" align="left"><bean:message
						key="ordine.label.fornitore" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="6" align="left"><html:text
						styleId="testoNormale"
						property="datiComunicazione.fornitore.codice" size="4"
						readonly="${noinput}"></html:text> <html:text
						styleId="testoNormale"
						property="datiComunicazione.fornitore.descrizione" size="50"
						readonly="${noinput}"></html:text> <html:submit
						styleClass="buttonImage" property="methodinserisciCom"
						disabled="${noinput}">
						<bean:message key="ordine.label.fornitore"
							bundle="acquisizioniLabels" />
					</html:submit></td>
				</sbn:disableAll>

			</tr>

			<tr>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="buono.label.dataBuono" bundle="acquisizioniLabels" /></td>
				<td scope="col" align="left"><html:text styleId="testoNormale"
					property="datiComunicazione.dataComunicazione" size="10"
					readonly="${noinput}"></html:text></td>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="ricerca.label.oggetto" bundle="acquisizioniLabels" /></td>
				<sbn:disableAll checkAttivita="SIF_DA_PERIODICO" inverted="true" >
					<td class="etichetta" scope="col" align="left"><html:select
						styleClass="testoNormale"
						property="datiComunicazione.tipoDocumento"
						onchange="this.form.submit();" disabled="${noinput}">
						<html:optionsCollection property="listaTipoDocumento"
							value="codice" label="descrizione" />
					</html:select></td>
				</sbn:disableAll>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="ricerca.label.direzione" bundle="acquisizioniLabels" /></td>
				<td class="etichetta" scope="col" align="left"><html:select
					styleClass="testoNormale"
					property="datiComunicazione.direzioneComunicazione"
					onchange="this.form.submit();" disabled="${noinput}">
					<html:optionsCollection property="listaDirezioneComunicazione"
						value="codice" label="descrizione" />
				</html:select></td>

			</tr>
			<tr>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="ricerca.label.tipoMsg" bundle="acquisizioniLabels" /></td>
				<sbn:disableAll checkAttivita="SIF_DA_PERIODICO" inverted="true" >
					<td class="etichetta" scope="col" align="left"><html:select
						styleClass="testoNormale"
						property="datiComunicazione.tipoMessaggio" disabled="${noinput}">
						<html:optionsCollection property="listaTipoMessaggio"
							value="codice" label="descrizione" />
					</html:select></td>
				</sbn:disableAll>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="ordine.label.tipoInvio" bundle="acquisizioniLabels" /></td>
				<td class="etichetta" scope="col" align="left"><html:select
					styleClass="testoNormale"
					property="datiComunicazione.tipoInvioComunicazione"
					disabled="${noinput}">
					<html:optionsCollection property="listaTipoInvio" value="codice"
						label="descrizione" />
				</html:select></td>

			</tr>
			<logic:equal name="navForm"
				property="datiComunicazione.tipoDocumento" value="">
				<tr>
					<td class="etichettaIntestazione" scope="col" align="left"><bean:message
						key="ricerca.button.operazionesuordine"
						bundle="acquisizioniLabels" /></td>

				</tr>
				<tr>
					<td scope="col" class="etichetta" align="left"><bean:message
						key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:select
						styleClass="testoNormale" property="testoVuoto"
						disabled="${noinputOrd}">
						<html:optionsCollection property="listaTipoOrdine" value="codice"
							label="descrizione" />
					</html:select></td>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="buono.label.anno" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						property="testoVuoto" size="4" readonly="${noinputOrd}"></html:text>
					</td>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="ricerca.label.codice" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						property="testoVuoto" size="4" readonly="${noinputOrd}"></html:text>
					<html:submit styleClass="buttonImage" property="methodinserisciCom"
						disabled="${noinputOrd}">
						<bean:message key="ricerca.button.ordine"
							bundle="acquisizioniLabels" />
					</html:submit></td>
				</tr>
				<tr>
					<td class="etichettaIntestazione" scope="col" align="left"><bean:message
						key="ricerca.label.fattura" bundle="acquisizioniLabels" /></td>
				</tr>
				<tr>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="buono.label.anno" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						size="4" readonly="${noinputFatt}" property="testoVuoto"></html:text>
					</td>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="ricerca.label.progr" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						size="4" readonly="${noinputFatt}" property="testoVuoto"></html:text>
					<html:submit styleClass="buttonImage" property="methodinserisciCom"
						disabled="${noinputFatt}">
						<bean:message key="ricerca.label.fattura"
							bundle="acquisizioniLabels" />
					</html:submit></td>

				</tr>

			</logic:equal>

			<logic:equal name="navForm"
				property="datiComunicazione.tipoDocumento" value="O">
				<tr>
					<td class="etichettaIntestazione" scope="col" align="left"><bean:message
						key="ricerca.button.operazionesuordine"
						bundle="acquisizioniLabels" /></td>
				</tr>
				<tr>
					<sbn:disableAll checkAttivita="SIF_DA_PERIODICO" inverted="true" >
						<td scope="col" class="etichetta" align="left"><bean:message
							key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:select
							styleClass="testoNormale"
							property="datiComunicazione.idDocumento.codice1"
							disabled="${noinputOrd}">
							<html:optionsCollection property="listaTipoOrdine" value="codice"
								label="descrizione" />
						</html:select></td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="buono.label.anno" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale"
							property="datiComunicazione.idDocumento.codice2" size="4"
							readonly="${noinputOrd}"></html:text></td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.codice" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale"
							property="datiComunicazione.idDocumento.codice3" size="4"
							readonly="${noinputOrd}"></html:text> <html:submit
							styleClass="buttonImage" property="methodinserisciCom"
							disabled="${noinputOrd}">
							<bean:message key="ricerca.button.ordine"
								bundle="acquisizioniLabels" />
						</html:submit></td>
					</sbn:disableAll>
				</tr>
				<sbn:checkAttivita idControllo="SIF_DA_PERIODICO" inverted="true" >
					<tr>
						<td class="etichettaIntestazione" scope="col" align="left"><bean:message
							key="ricerca.label.fattura" bundle="acquisizioniLabels" /></td>
					</tr>
					<tr>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="buono.label.anno" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text styleId="testoNormale"
							size="4" readonly="${noinputFatt}" property="testoVuoto"></html:text>
						</td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.progr" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text styleId="testoNormale"
							size="4" readonly="${noinputFatt}" property="testoVuoto"></html:text>
						<html:submit styleClass="buttonImage" property="methodinserisciCom"
							disabled="${noinputFatt}">
							<bean:message key="ricerca.label.fattura"
								bundle="acquisizioniLabels" />
						</html:submit></td>
					</tr>
				</sbn:checkAttivita>

			</logic:equal>
			<logic:equal name="navForm"
				property="datiComunicazione.tipoDocumento" value="F">
				<tr>
					<td class="etichettaIntestazione" scope="col" align="left"><bean:message
						key="ricerca.button.operazionesuordine"
						bundle="acquisizioniLabels" /></td>

				</tr>

				<tr>
					<td scope="col" class="etichetta" align="left"><bean:message
						key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:select
						styleClass="testoNormale" property="testoVuoto"
						disabled="${noinputOrd}">
						<html:optionsCollection property="listaTipoOrdine" value="codice"
							label="descrizione" />
					</html:select></td>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="buono.label.anno" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						property="testoVuoto" size="4" readonly="${noinputOrd}"></html:text>
					</td>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="ricerca.label.codice" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						property="testoVuoto" size="4" readonly="${noinputOrd}"></html:text>
					<html:submit styleClass="buttonImage" property="methodinserisciCom"
						disabled="${noinputOrd}">
						<bean:message key="ricerca.button.ordine"
							bundle="acquisizioniLabels" />
					</html:submit></td>
				</tr>
				<tr>
					<td class="etichettaIntestazione" scope="col" align="left"><bean:message
						key="ricerca.label.fattura" bundle="acquisizioniLabels" /></td>
				</tr>
				<tr>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="buono.label.anno" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						property="datiComunicazione.idDocumento.codice2" size="4"
						readonly="${noinputFatt}"></html:text></td>
					<td class="etichetta" scope="col" align="left"><bean:message
						key="ricerca.label.progr" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
						property="datiComunicazione.idDocumento.codice3" size="4"
						readonly="${noinputFatt}"></html:text> <html:submit
						styleClass="buttonImage" property="methodinserisciCom"
						disabled="${noinputFatt}">
						<bean:message key="ricerca.label.fattura"
							bundle="acquisizioniLabels" />
					</html:submit></td>
				</tr>
			</logic:equal>

			<tr>
				<td class="etichetta" align="left" scope="col"><bean:message
					key="ordine.label.noteEtic" bundle="acquisizioniLabels" /></td>
				<td class="testoNormale" colspan="6" scope="col" align="left">
				<html:textarea styleId="testoNormale"
					property="datiComunicazione.noteComunicazione" rows="3" cols="100"
					readonly="${noinput}"></html:textarea> <c:choose>
					<c:when test="${navForm.disabilitaTutto eq false}">
						<sbn:tastiera limit="80"
							property="datiComunicazione.noteComunicazione"
							name="navForm"></sbn:tastiera>
					</c:when>
				</c:choose></td>

			</tr>

			<tr>
				<td colspan="6" class="etichetta">&nbsp;</td>
			</tr>
		</table>

		<!-- tabella bottoni --></div>
		<div id="divFooter"><c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<table align="center" border="0" style="height: 40px">
					<tr>
						<td><html:submit styleClass="pulsanti"
							property="methodinserisciCom">
							<bean:message key="ricerca.button.salva"
								bundle="acquisizioniLabels" />
						</html:submit> <html:submit styleClass="pulsanti" property="methodinserisciCom">
							<bean:message key="ricerca.button.ripristina"
								bundle="acquisizioniLabels" /> 	</html:submit>
							<sbn:checkAttivita idControllo="ESAME_FASCICOLI">
								<sbn:checkAttivita idControllo="SIF_DA_PERIODICO"
									inverted="true">
									<html:submit styleClass="pulsanti"
										property="methodinserisciCom">
										<bean:message key="ricerca.button.periodici"
											bundle="acquisizioniLabels" />
									</html:submit>
								</sbn:checkAttivita>
							</sbn:checkAttivita>
						 <html:submit styleClass="pulsanti" property="methodinserisciCom">
							<bean:message key="ricerca.button.indietro"
								bundle="acquisizioniLabels" />
						</html:submit>
						<logic:equal name="navForm"
							property="visibilitaIndietroLS" value="true">
							<html:submit styleClass="pulsanti" property="methodinserisciCom">
								<bean:message key="ricerca.button.scegli"
									bundle="acquisizioniLabels" />
							</html:submit>
						</logic:equal></td>
					</tr>
				</table>
				<!-- fine tabella bottoni -->
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
