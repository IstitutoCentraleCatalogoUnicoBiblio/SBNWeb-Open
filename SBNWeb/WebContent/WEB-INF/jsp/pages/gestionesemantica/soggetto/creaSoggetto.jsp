<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/CreaSoggetto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<c:if test="${navForm.enableTit}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" flush="true" />
			</c:if>
			<logic:notEmpty name="navForm"
				property="listaStatoControllo">
				<logic:notEmpty name="navForm" property="listaTipoSoggetto">
					<table width="100%" border="0">
						<tr>
							<td class="etichetta">
								Base dati in aggiornamento Locale
							</td>
						</tr>
					</table>
					<table width="100%" border="0">
						<tr>
							<td class="etichetta" scope="col" align="center">
								<bean:message key="crea.cid" bundle="gestioneSemanticaLabels" />
							</td>
							<td>
								<html:text styleId="testoNormale" property="cid" readonly="true">
								</html:text>
							</td>
							<td class="etichetta">
								<bean:message key="crea.soggettario"
									bundle="gestioneSemanticaLabels" />
							</td>
							<td>
								<html:select styleClass="testoNormale"
									property="ricercaComune.codSoggettario"
									disabled="${navForm.ricercaComune.codSoggettario ne ''}">
									<html:optionsCollection property="listaSoggettari"
										value="codice" label="descrizione" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
							<td><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" /></td>
							<td><html:select styleClass="testoNormale"
									property="edizione" >
									<html:optionsCollection property="listaEdizioni"
										value="cd_tabellaTrim" label="ds_tabella" />
								</html:select></td>
						</tr>
						<tr>
							<td class="etichetta">
								<bean:message key="crea.statoDiControllo"
									bundle="gestioneSemanticaLabels" />
							</td>
							<td>
								<html:select styleClass="testoNormale"
									property="codStatoControllo">
									<html:optionsCollection property="listaStatoControllo"
										value="codice" label="descrizione" />
								</html:select>
							</td>
							<td class="etichetta">
								<bean:message key="crea.tipoDiSoggetto"
									bundle="gestioneSemanticaLabels" />
							</td>
							<td>
								<html:select styleClass="testoNormale"
									property="codTipoSoggetto">
									<html:optionsCollection property="listaTipoSoggetto"
										value="codice" label="descrizione" />
								</html:select>
							</td>
						</tr>
					</table>
					<table width="100%" border="0">
						<tr>
							<td align="center" class="etichetta">
								<bean:message key="crea.testo" bundle="gestioneSemanticaLabels" />
							</td>
						</tr>
						<tr>
							<td>
								<html:textarea styleId="testoNormale" property="testo" cols="90"
									rows="6"></html:textarea>
								<sbn:tastiera property="testo" limit="240"
									name="CreaSoggettoForm" />
							</td>
						</tr>
						<tr>
							<td class="etichetta" align="center">
								<bean:message key="crea.note" bundle="gestioneSemanticaLabels" />
							</td>
						</tr>
						<tr>
							<td>
								<html:textarea styleId="testoNormale" property="note" cols="90"
									rows="6"></html:textarea>
								<sbn:tastiera limit="240" name="CreaSoggettoForm"
									property="note" />
							</td>
						</tr>
					</table>

					<table width="100%" border="0">
						<tr>
							<td align="left" class="etichetta" scope="col">
								<bean:message key="crea.inserito"
									bundle="gestioneSemanticaLabels" />
							</td>
							<td class="etichetta">
								<bean:message key="crea.il" bundle="gestioneSemanticaLabels" />
							</td>
							<td>
								<html:text styleId="testoNormale" property="dataInserimento"
									size="14" maxlength="20" readonly="true"></html:text>
							</td>
							<td class="etichetta">
								<bean:message key="crea.modificato"
									bundle="gestioneSemanticaLabels" />
							</td>
							<td class="etichetta">
								<bean:message key="crea.il" bundle="gestioneSemanticaLabels" />
							</td>
							<td>
								<html:text styleId="testoNormale" property="dataModifica"
									size="14" maxlength="20" readonly="true"></html:text>
							</td>
						</tr>
					</table>
					<table width="100%" border="0">
						<tr>
							<td class="etichetta">
								<bean:message key="crea.titoliCollegati"
									bundle="gestioneSemanticaLabels" />
							</td>
							<td class="etichetta">
								<bean:message key="crea.polo" bundle="gestioneSemanticaLabels" />
							<td>
								<html:text styleId="testoNormale" property="numNotiziePolo"
									size="15" maxlength="10" readonly="true"></html:text>
							</td>
							<td class="etichetta">
								<bean:message key="crea.biblio" bundle="gestioneSemanticaLabels" />
							<td>
								<html:text styleId="testoNormale" property="numNotizieBiblio"
									size="15" maxlength="10" readonly="true"></html:text>
							</td>
						</tr>
						<tr>

						</tr>
					</table>
				</logic:notEmpty>
			</logic:notEmpty>
		</div>
		<div id="divFooter">
			<logic:notEmpty name="navForm"
				property="listaStatoControllo">
				<logic:notEmpty name="navForm" property="listaTipoSoggetto">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodCreaSog">
									<bean:message key="button.salvaSog"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodCreaSog">
									<bean:message key="button.stampa"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodCreaSog">
									<bean:message key="button.annulla"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</logic:notEmpty>
			</logic:notEmpty>
		</div>
	</sbn:navform>
</layout:page>
