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
<bean-struts:define id="NOFornBibl" value="false" />

<c:choose>
	<c:when test="${esaminaFornitoreForm.disabilitaTutto}">
		<bean-struts:define id="noinput" value="true" />
		<bean-struts:define id="NOFornBibl" value="true" />
	</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fornitori/esaminaFornitore.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors
			bundle="acquisizioniMessages" /></div>
		<table width="100%" align="center">
			<tr>
				<!-- tabella corpo COLONNA + LARGA -->
				<td align="left" valign="top" width="100%">
				<table width="100%">

					<tr>
						<td class="etichetta" width="15%" scope="col" align="left"><bean:message
							key="ricerca.label.codForn" bundle="acquisizioniLabels" /></td>
						<td colspan="5" scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.codFornitore" size="5"
							readonly="true"></html:text> <html:text styleId="testoNormale"
							property="fornitore.nomeFornitore" size="80" maxlength="50"
							readonly="${noinput}"></html:text> <c:choose>
							<c:when test="${esaminaFornitoreForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" name="esaminaFornitoreForm"
									property="fornitore.nomeFornitore"></sbn:tastiera>
							</c:when>
						</c:choose></td>
					</tr>
					<tr>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.codBibl" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="datiFornitore.codBibl" size="5"
							readonly="true"></html:text></td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.unitaorgShort" bundle="acquisizioniLabels" />
						</td>

						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.unitaOrg" size="80"
							maxlength="50" readonly="${noinput}"></html:text></td>

					</tr>
					<tr>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.indirizzo" bundle="acquisizioniLabels" /></td>
						<td colspan="4" scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.indirizzo" size="70"
							maxlength="70" readonly="${noinput}"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.cPostale" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.casellaPostale"
							size="20" maxlength="20" readonly="${noinput}"></html:text></td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.citta" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left" colspan="2"><html:text
							styleId="testoNormale" property="fornitore.citta" size="20"
							maxlength="20" readonly="${noinput}"></html:text> <!--
                        </td>
 						<td  class="etichetta"  scope="col" align="left">
	                        --> &nbsp;<bean:message
							key="ricerca.label.codAvvPostale" bundle="acquisizioniLabels" />
						<!--
 						</td>
                        <td  scope="col" align="left">
                        	--> &nbsp;<html:text styleId="testoNormale"
							property="fornitore.cap" size="10" maxlength="10"
							readonly="${noinput}"></html:text></td>

					</tr>
					<tr>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.telefono" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.telefono" size="20"
							readonly="${noinput}"></html:text></td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.fax" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.fax" size="20"
							readonly="${noinput}"></html:text></td>

					</tr>
					<tr>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.codFiscale" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.codiceFiscale"
							size="18" maxlength="18" readonly="${noinput}"></html:text></td>
						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.partitaIVA" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.partitaIva" size="18"
							maxlength="18" readonly="${noinput}"></html:text></td>
					</tr>

					<tr>

						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.tipoForn" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:select
							styleClass="testoNormale" property="fornitore.tipoPartner"
							style="width:40px;" disabled="${noinput}">
							<html:optionsCollection property="listaTipoForn" value="codice"
								label="descrizioneCodice" />
						</html:select></td>

						<td class="etichetta" scope="col" align="left"><bean:message
							key="ricerca.label.email" bundle="acquisizioniLabels" /></td>
						<td scope="col" align="left"><html:text
							styleId="testoNormale" property="fornitore.email" size="50"
							maxlength="50" readonly="${noinput}"></html:text></td>

					</tr>

					<tr>
						<!--  Maggio 2013: nella linea fornitori nel caso di un fornitore/editore non si possono modificare
						i campi paese e provincia vengono disabilitati con controllo su presenza codice regione	-->
						<c:choose>
							<c:when test="${esaminaFornitoreForm.editore eq 'SI'}">

								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.paese" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="fornitore.paese"
									style="width:50px;" disabled="${noinput}">
									<html:optionsCollection property="listaPaeseForn"
										value="codice" label="descrizioneCodice" />
								</html:select></td>

								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.provincia" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="fornitore.provincia"
									style="width:50px;" disabled="${noinput}">
									<html:optionsCollection property="listaProvinciaForn"
										value="codice" label="descrizioneCodice" />
								</html:select> <bean:message key="ricerca.label.regione"
									bundle="acquisizioniLabels" /> <html:select
									styleClass="testoNormale" property="fornitore.regione"
									style="width:120px;" disabled="${noinput}">
									<html:optionsCollection property="listaRegioneForn"
										value="codice" label="descrizione" />
								</html:select></td>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${esaminaFornitoreForm.regioneForn eq ''}">
									<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.paese" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="fornitore.paese"
									style="width:50px;" disabled="${noinput}">
									<html:optionsCollection property="listaPaeseForn"
										value="codice" label="descrizione" />
								</html:select></td>

								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.provincia" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="fornitore.provincia"
									style="width:50px;" disabled="${noinput}">
									<html:optionsCollection property="listaProvinciaForn"
										value="codice" label="descrizioneCodice" />
								</html:select></td>
									</c:when>
									<c:otherwise>
									<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.paese" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="fornitore.paese"
									style="width:50px;" disabled="true">
									<html:optionsCollection property="listaPaeseForn"
										value="codice" label="descrizioneCodice" />
								</html:select></td>

								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.provincia" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="fornitore.provincia"
									style="width:50px;" disabled="true">
									<html:optionsCollection property="listaProvinciaForn"
										value="codice" label="descrizioneCodice" />
								</html:select></td>
									</c:otherwise>
								</c:choose>




							</c:otherwise>
						</c:choose>


					</tr>
					<tr>
						<td colspan="6" class="etichettaIntestazione" align="left"
							scope="col"><bean:message key="ordine.label.noteEtic"
							bundle="acquisizioniLabels" /></td>

					</tr>

					<tr>

						<td colspan="6" scope="col" align="left"><html:textarea
							styleId="testoNormale" property="fornitore.note" rows="1"
							cols="100" readonly="${noinput}"></html:textarea> <c:choose>
							<c:when test="${esaminaFornitoreForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" name="esaminaFornitoreForm"
									property="fornitore.note"></sbn:tastiera>
							</c:when>
						</c:choose></td>

					</tr>

				</table>

				<c:choose>
					<c:when test="${esaminaFornitoreForm.editore eq 'SI'}">

						<table border="0" bordercolor="#dde8f0">
							<tr>
								<th width="100" class="etichetta"><bean:message
									key="ricerca.label.isbnEditore" bundle="acquisizioniLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0"><bean:message
									key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
									styleClass="buttonImageDelLine"
									property="methodEsaminaFornitore" title="Cancella Num.Standard">
									<bean:message key="button.canNumStandard"
										bundle="gestioneBibliograficaLabels" />
								</html:submit> <html:submit styleClass="buttonImageNewLine"
									property="methodEsaminaFornitore"
									title="Inserisci Num.Standard">
									<bean:message key="button.insNumStandard"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></th>
							</tr>
							<logic:iterate id="itemIsbnEdit"
								property="fornitore.listaNumStandard"
								name="esaminaFornitoreForm" indexId="idxIsbnEdit">
								<tr>
									<td></td>
									<td bgcolor="#FFCC99"><html:text name="itemIsbnEdit"
										property="campoUno" indexed="true" /></td>
									<td bgcolor="#FFCC99"><html:radio
										property="selezRadioNumStandard" value="${idxIsbnEdit}" /></td>
								</tr>
							</logic:iterate>
						</table>

					</c:when>
					<c:otherwise>


						<table width="100%" border="0" bgcolor="#FFCC99">
							<tr>
								<td width="15%" class="etichetta" scope="col" align="left">
								<bean:message key="ordine.label.tipoPag"
									bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:text
									styleId="testoNormale" property="datiFornitore.tipoPagamento"
									size="20" maxlength="50" readonly="${NOFornBibl}"></html:text>
								</td>
								<td class="etichetta" scope="col" align="left"><bean:message
									key="ordine.label.valuta" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:select
									styleClass="testoNormale" property="datiFornitore.valuta"
									style="width:60px;" disabled="${NOFornBibl}">
									<html:optionsCollection property="listaValuta" value="codice1"
										label="codice2" />
								</html:select></td>

								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.codCliente" bundle="acquisizioniLabels" />
								</td>
								<td scope="col" align="left"><html:text
									styleId="testoNormale" property="datiFornitore.codCliente"
									size="20" maxlength="40" readonly="${NOFornBibl}"></html:text>
								</td>

							</tr>

							<tr>
								<td colspan="6" class="etichettaIntestazione" align="left"
									scope="col"><bean:message key="ricerca.label.contatto"
									bundle="acquisizioniLabels" /></td>

							</tr>

							<tr>
								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.nome" bundle="acquisizioniLabels" /></td>
								<td colspan="5" scope="col" align="left"><html:text
									styleId="testoNormale" property="datiFornitore.nomContatto"
									size="50" readonly="${NOFornBibl}"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.tel" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:text
									styleId="testoNormale" property="datiFornitore.telContatto"
									size="20" readonly="${NOFornBibl}"></html:text></td>
								<td class="etichetta" scope="col" align="left"><bean:message
									key="ricerca.label.fax" bundle="acquisizioniLabels" /></td>
								<td scope="col" align="left"><html:text
									styleId="testoNormale" property="datiFornitore.faxContatto"
									size="20" readonly="${NOFornBibl}"></html:text></td>

							</tr>



							<tr>
								<td colspan="6" class="etichetta">&nbsp;</td>
							</tr>
						</table>


						<!--  Maggio 2013: nella linea fornitori devono essere presenti i dati dell'editore ma disabilitati	-->
						<table border="0" bordercolor="#dde8f0">
							<tr>
								<th width="100" class="etichetta"><bean:message
									key="ricerca.label.regione" bundle="acquisizioniLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0"><html:select
									styleClass="testoNormale" property="fornitore.regione"
									style="width:120px;" disabled="true">
									<html:optionsCollection property="listaRegioneForn"
										value="codice" label="descrizione" />
								</html:select></th>
								<th width="100" class="etichetta"></th>
								<th width="100" class="etichetta"><bean:message
									key="ricerca.label.isbnEditore" bundle="acquisizioniLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0"><bean:message
									key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
							</tr>
							<logic:iterate id="itemIsbnEdit"
								property="fornitore.listaNumStandard"
								name="esaminaFornitoreForm" indexId="idxIsbnEdit">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td bgcolor="#FFCC99"><html:text name="itemIsbnEdit"
										property="campoUno" indexed="true" readonly="true" /></td>
								</tr>
							</logic:iterate>
						</table>



					</c:otherwise>
				</c:choose></td>
			</tr>

			<tr>
				<td class="etichetta">&nbsp;</td>
			</tr>
		</table>
		</div>

		<div id="divFooter"><c:choose>
			<c:when test="${esaminaFornitoreForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>

				<table align="center" border="0" style="height: 40px">
					<tr>
						<td valign="top" align="center"><logic:equal
							name="esaminaFornitoreForm" property="enableScorrimento"
							value="true">
							<html:submit styleClass="pulsanti"
								property="methodEsaminaFornitore">
								<bean:message key="ricerca.button.scorriIndietro"
									bundle="acquisizioniLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti"
								property="methodEsaminaFornitore">
								<bean:message key="ricerca.button.scorriAvanti"
									bundle="acquisizioniLabels" />
							</html:submit>
						</logic:equal> <logic:equal name="esaminaFornitoreForm"
							property="disabilitaTutto" value="false">
							<sbn:checkAttivita idControllo="GESTIONE">

								<html:submit styleClass="pulsanti"
									property="methodEsaminaFornitore">
									<bean:message key="ricerca.button.salva"
										bundle="acquisizioniLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti"
									property="methodEsaminaFornitore">
									<bean:message key="ricerca.button.ripristina"
										bundle="acquisizioniLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti"
									property="methodEsaminaFornitore">
									<bean:message key="ricerca.button.cancella"
										bundle="acquisizioniLabels" />
								</html:submit>



								<c:choose>
									<c:when test="${esaminaFornitoreForm.editore ne 'SI'}">
										<c:choose>
											<c:when test="${esaminaFornitoreForm.gestProf}">
												<html:submit styleClass="pulsanti"
													property="methodEsaminaFornitore">
													<bean:message key="button.crea.profiliAcquisto"
														bundle="acquisizioniLabels" />
												</html:submit>
											</c:when>
										</c:choose>
										<!--  Maggio 2013: nella linea fornitori il bottone importa da biblioteca va tolto da esamina
								mail sulla produzione editoriale di rox, aste e scogna
								<html:submit styleClass="pulsanti" property="methodEsaminaFornitore">
									<bean:message key="button.crea.importaBib" bundle="acquisizioniLabels" />
								</html:submit>
								-->
									</c:when>
								</c:choose>

							</sbn:checkAttivita>

						</logic:equal> <html:submit styleClass="pulsanti"
							property="methodEsaminaFornitore">
							<bean:message key="ricerca.button.indietro"
								bundle="acquisizioniLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
