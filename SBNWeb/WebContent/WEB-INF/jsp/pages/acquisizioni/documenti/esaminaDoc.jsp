<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput" value="false" />

<sbn:checkAttivita idControllo="STATO_SUGG" inverted="true">
	<bean-struts:define id="noinput" value="true" />
</sbn:checkAttivita>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/documenti/esaminaDoc.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="acquisizioniMessages" />
			</div>
			<sbn:disableAll disabled="${navForm.conferma}">
			<table width="100%" border="0" cellpadding="1" cellspacing="1">
				<tr>
					<td colspan="7" class="etichetta">&nbsp;</td>
				</tr>
				<tr>
					<td class="etichetta" width="15%" scope="col" align="left"><bean:message
							key="ricerca.label.codBibl" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
							property="datiDocumento.codBibl" size="4" disabled="true"></html:text>
					</td>
					<td class="etichetta" scope="col" align="right"><bean:message
							key="buono.label.numero" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
							property="datiDocumento.codDocumento" size="5" disabled="true"></html:text>
					</td>
					<td class="etichetta" scope="col" align="left">&nbsp;</td>
				</tr>

				<tr>
					<td class="etichetta" width="15%" scope="col" align="left"><bean:message
							key="ricerca.label.codUtente" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left">
						<!--							<html:text styleId="testoNormale"  property="datiDocumento.utente.codice1" size="5" disabled="true" ></html:text>-->
						<html:text styleId="testoNormale"
							property="datiDocumento.utente.codice2" size="20" maxlength="25"
							disabled="true"></html:text></td>
					<td width="10%" scope="col" class="etichetta" align="left"><bean:message
							key="ricerca.label.nome" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="2" align="left"><html:text
							styleId="testoNormale" property="datiDocumento.utente.codice3"
							size="30" disabled="true"></html:text></td>
				</tr>

				<tr>
					<td class="etichetta" scope="col" align="right"><bean:message
							key="buono.label.statoBuono" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="2" align="left"><html:select
							styleClass="testoNormale"
							property="datiDocumento.statoSuggerimentoDocumento"
							disabled="true">
							<html:optionsCollection property="listaStatoSuggerimento"
								value="codice" label="descrizione" />
						</html:select></td>
				</tr>

				<tr>
					<td class="etichetta" scope="col" valign="top" align="left"><bean:message
							key="ordine.label.bid" bundle="acquisizioniLabels" /></td>
					<td scope="col" valign="top" align="left"><html:text
							styleId="testoNormale" property="datiDocumento.titolo.codice"
							size="10" maxlength="10" disabled="${noinput}"></html:text> <!--	     	    			<html:link action="/gestionebibliografica/titolo/interrogazioneTitolo.do" ><img border="0"   alt="ricerca titolo"  src='<c:url value="/images/lente.GIF" />'/></html:link>-->
						<html:submit styleClass="buttonImage" property="methodEsaminaDoc"
							disabled="${noinput}">
							<bean:message key="ordine.bottone.searchTit"
								bundle="acquisizioniLabels" />
						</html:submit></td>
					<td class="etichetta" scope="col" colspan="2" valign="top"
						align="right"><bean:message key="ricerca.label.dataIns"
							bundle="acquisizioniLabels" /> <html:text styleId="testoNormale"
							property="datiDocumento.dataIns" size="10" disabled="true"></html:text>
					</td>
					<td valign="top" class="etichetta" scope="col" colspan="2"
						align="left"><bean:message key="ricerca.label.dataAgg"
							bundle="acquisizioniLabels" /> <html:text styleId="testoNormale"
							property="datiDocumento.dataAgg" size="10" disabled="true"></html:text>
					</td>

				</tr>

				<tr>
					<td class="etichetta" scope="col" align="left"><bean:message
							key="ordine.label.tabTitolo" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="6" align="left">
						<!--							<html:text styleId="testoNormale"  property="datiDocumento.titolo.descrizione" size="90" disabled="${noinput}"></html:text>-->
						<bean-struts:write name="navForm"
							property="datiDocumento.titolo.descrizione" /></td>

				</tr>

				<tr>
					<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.autore" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="6" align="left"><html:text
							styleId="testoNormale" property="datiDocumento.primoAutore"
							size="90" disabled="${noinput}"></html:text></td>

				</tr>

				<tr>
					<td class="etichetta" scope="col" align="left"><bean:message
							key="ordine.label.noteEtic" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="6" align="left"><html:textarea
							styleId="testoNormale" property="datiDocumento.noteDocumento"
							rows="1" cols="50" disabled="${noinput}"></html:textarea> <c:choose>
							<c:when test="${navForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" property="datiDocumento.noteDocumento"
									name="esaminaDocForm"></sbn:tastiera>
							</c:when>
						</c:choose></td>

				</tr>

				<tr>
					<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.editore" bundle="acquisizioniLabels" /></td>
					<td scope="col" colspan="3" align="left"><html:text
							styleId="testoNormale" property="datiDocumento.editore" size="50"
							disabled="${noinput}"></html:text></td>
					<td class="etichetta" scope="col" align="right"><bean:message
							key="ordine.label.luogo" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
							property="datiDocumento.luogoEdizione" size="10"
							disabled="${noinput}"></html:text></td>

				</tr>

				<tr>
					<td width="10%" scope="col" class="etichetta" align="left"><bean:message
							key="ricerca.label.paese" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:select style="width:100px"
							styleClass="testoNormale" property="datiDocumento.paese.codice"
							disabled="${noinput}">
							<html:optionsCollection property="listaPaesi" value="codice"
								label="descrizione" />
						</html:select></td>
					<td width="10%" scope="col" class="etichetta" align="right"><bean:message
							key="ricerca.label.lingua" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:select style="width:50px;"
							styleClass="testoNormale" property="datiDocumento.lingua.codice"
							disabled="${noinput}">
							<html:optionsCollection property="listaLingue" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					<td class="etichetta" scope="col" align="right"><bean:message
							key="buono.label.anno" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
							property="datiDocumento.annoEdizione" size="4"
							disabled="${noinput}"></html:text></td>

				</tr>

				<tr>
					<td class="etichetta" align="left" scope="col"><bean:message
							key="ordine.label.noteXLettore" bundle="acquisizioniLabels" /></td>
					<td colspan="5" scope="col" align="left"><html:textarea
							styleId="testoNormale" property="datiDocumento.msgPerLettore"
							rows="1" cols="50" ></html:textarea>
							<sbn:tastiera limit="80" property="datiDocumento.msgPerLettore"	name="esaminaDocForm"/>

						</td>

				</tr>

				<tr>
					<td colspan="7" class="etichetta">&nbsp;</td>
				</tr>

			</table>
			</sbn:disableAll>
		</div>

		<div id="divFooter">
			<c:choose>
				<c:when test="${navForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<table align="center" border="0" style="height: 40px">
						<tr>
							<td valign="top"><logic:equal name="navForm"
									property="enableScorrimento" value="true">
									<html:submit styleClass="pulsanti" property="methodEsaminaDoc">
										<bean:message key="ricerca.button.scorriIndietro"
											bundle="acquisizioniLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" property="methodEsaminaDoc">
										<bean:message key="ricerca.button.scorriAvanti"
											bundle="acquisizioniLabels" />
									</html:submit>
								</logic:equal>

								<sbn:bottoniera buttons="pulsanti"/>

								 <html:submit styleClass="pulsanti" property="methodEsaminaDoc">
									<bean:message key="ricerca.button.stampa"
										bundle="acquisizioniLabels" />
								</html:submit> <html:submit styleClass="pulsanti" property="methodEsaminaDoc">
									<bean:message key="ricerca.button.indietro"
										bundle="acquisizioniLabels" />
								</html:submit></td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>

		</div>
	</sbn:navform>
</layout:page>
