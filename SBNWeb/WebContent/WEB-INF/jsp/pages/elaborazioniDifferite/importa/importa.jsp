<!--	SBNWeb - Rifacimento ClientServer
		JSP per il servizio di Import
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

<%@ page
	import="it.iccu.sbn.web.actionforms.elaborazioniDifferite.importa.ImportaForm"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/elaborazioniDifferite/importa/importa.do"
		enctype="multipart/form-data">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

		<!-- begin MENU NAVIGAZIONE TABs -->
		<!-- end MENU NAVIGAZIONE TABs -->

		<!-- begin parte comune a tutti i tabs -->
		<!-- end parte comune a tutti i tabs -->

		<!-- begin campi IMPORT -->
		<table class="sintetica" width="100%">
			<tr>
				<td>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="2"><bean:message
							key="label.caricarecordunimarcfile" bundle="importaLabels" /></td>
						<td width="80%" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="label.selezionafile"
							bundle="importaLabels" /></td>
						<td class="etichetta"><html:file property="fileIdList"
							value="importa.fileIdListPathNameLocal" size="60" /></td>
						<td class="etichetta"><html:submit property="methodMap_importa">
							<bean:message key="button.caricafile" bundle="importaLabels" />
						</html:submit></td>
					</tr>
					<tr>

						<td class="etichetta" colspan="2">
							<bean:message key="caricamento.firstCar" bundle="importaLabels" />
							<html:radio	property="verificaTipoCaricamento" value="F"/>
							<bean:message key="caricamento.nextCar" bundle="importaLabels" />
							<html:radio	property="verificaTipoCaricamento" value="N"/>
							<bean:message key="label.numrichiesta" bundle="importaLabels" />
								<html:text styleId="testoNormale" property="idRichiestaCaricamento" size="10" maxlength="9" readonly="${noinput}"></html:text>
						</td>
						<td class="etichetta"><html:submit property="methodMap_importa">
							<bean:message key="button.richiestaImport" bundle="importaLabels" />
						</html:submit></td>
					</tr>

				</table>
				</td>
			</tr>

			<tr><td>&nbsp;</td></tr>

			<tr>
				<td>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="3"><bean:message
							key="label.verificabid" bundle="importaLabels" /></td>
						<td width="65%">&nbsp;</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="label.numrichiesta" bundle="importaLabels" />
							<html:text styleId="testoNormale" property="idRichiestaVerificaBid" size="10" maxlength="9" readonly="${noinput}"></html:text>
						</td>
						<td class="etichetta" colspan="2">
							<bean:message key="ricerca.polo" bundle="importaLabels" />
							<html:radio	property="verificaBidPoloIndice" value="P"/>
							<bean:message key="ricerca.indice" bundle="importaLabels" />
							<html:radio	property="verificaBidPoloIndice" value="I"/>
						</td>
						<td class="etichetta">
							<html:submit property="methodMap_importa">
							<bean:message key="button.richiestaVerificaBid" bundle="importaLabels" />
							</html:submit>
						</td>
					</tr>
				</table>
				</td>
			</tr>

			<tr><td>&nbsp;</td></tr>

			<tr>
				<td>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="2"><bean:message
							key="label.trattaetichetteunimarc" bundle="importaLabels" /></td>
						<td width="65%" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="label.ricerca.selezionaetichetta" bundle="importaLabels" />&nbsp;
							<html:select property="etichettaSelez">
							<html:optionsCollection property="listaEtichette" value="codice" label="descrizione" />
							</html:select>
						</td>
						<td class="etichetta">
							<bean:message key="label.numrichiesta" bundle="importaLabels" />&nbsp;
							<html:text styleId="testoNormale" property="idRichiesta" size="10" maxlength="9" readonly="${noinput}"></html:text>
						</td>
						<td class="etichetta" colspan="2">
							<html:submit property="methodMap_importa">
							<bean:message key="button.richiestaEtichette" bundle="importaLabels" />
							</html:submit>
						</td>
					</tr>

				</table>
				</td>
			</tr>

	<tr><td>&nbsp;</td></tr>

			<tr>
				<td>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="3"><bean:message
							key="label.cancellaTabelleAppoggio" bundle="importaLabels" /></td>
						<td width="65%">&nbsp;</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="label.numrichiesta" bundle="importaLabels" />
							<html:text styleId="testoNormale" property="idRichiestaCancellazione" size="10" maxlength="9" readonly="${noinput}"></html:text>
						</td>
						<td class="etichetta">
							<html:submit property="methodMap_importa">
							<bean:message key="button.richiestacancellaTabelleAppoggio" bundle="importaLabels" />
							</html:submit>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<!-- end campi IMPORT -->

		</div>

	</sbn:navform>
</layout:page>
