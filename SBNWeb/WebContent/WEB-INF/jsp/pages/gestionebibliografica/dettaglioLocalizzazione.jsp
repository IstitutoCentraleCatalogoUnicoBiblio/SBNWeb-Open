<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
		almaviva2 - Inizio Codifica Agosto 2006
-->


<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>


	<sbn:navform action="/gestionebibliografica/dettaglioLocalizzazione.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>

		<table border="0">
			<tr>
				<td class="etichetta">
					<bean:message key="ricerca.titoloRiferimento" bundle="gestioneBibliograficaLabels" />:
				</td>
				<td width="20" class="testoNormale">
					<html:text property="idOggColl" size="10" readonly="true"></html:text>
				</td>
				<td class="etichetta">
					<html:text property="descOggColl" size="50" readonly="true"></html:text>
				</td>
			</tr>
		</table>
		<hr color="#dde8f0" />

		<c:choose>
			<c:when test="${navForm.tipoProspettazione eq 'DET'}">
				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.denominazione" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.descrBiblioteca" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.idAnagBibl" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.IDAnagrafe" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.codSbnBibl" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.IDSbn" size="100" readonly="true"></html:text>
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnaturaAntica" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.consistenza" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.note" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.valoreM" size="100" readonly="true"
							title="${navForm.localizzazione.valoreM}"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.formatoElettronico" size="100" readonly="true"
							title="${navForm.localizzazione.formatoElettronico}"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.tipoDigitalizzazione" size="100" readonly="true"
							title="${navForm.descTipoDigitPolo}"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100" readonly="true"></html:textarea>
						</td>
					</tr>
				</table>
			</c:when>

			<c:when test="${navForm.tipoProspettazione eq 'GESTCONS'}">
				<table border="0" bgcolor="#FFCC99">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.consistenza.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="consistenzaPolo" cols="100" rows="4" readonly="true" ></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uri.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="uriPolo" rows="4" cols="100" readonly="true" ></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.denominazione.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.descrBiblioteca" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100"></html:text>
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.segnaturaAntica" cols="100" rows="2"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.consistenza" cols="100" rows="4"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.valoreM" style="width:40px">
							<html:optionsCollection property="listaMutiloM" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.note" cols="100" rows="3"></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.formatoElettronico" style="width:40px">
							<html:optionsCollection property="listaFormatoElettr" value="codice"
										label="descrizioneCodice" />
						</html:select></td>

						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:select property="localizzazione.tipoDigitalizzazione" style="width:40px">
							<html:optionsCollection property="listaTipoDigital" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100"></html:textarea>
						</td>
					</tr>
				</table>

				<hr color="#dde8f0" />

			</c:when>


			<c:when test="${navForm.tipoProspettazione eq 'listaInventariTitolo'}">
				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.denominazione.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.descrBiblioteca" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100"></html:text>
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.segnaturaAntica" cols="100" rows="2"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.consistenza" cols="100" rows="4"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.valoreM" style="width:40px">
							<html:optionsCollection property="listaMutiloM" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.note" cols="100" rows="3"></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.formatoElettronico" style="width:40px">
							<html:optionsCollection property="listaFormatoElettr" value="codice"
										label="descrizioneCodice" />
						</html:select></td>

						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:select property="localizzazione.tipoDigitalizzazione" style="width:40px">
							<html:optionsCollection property="listaTipoDigital" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100"></html:textarea>
						</td>
					</tr>
				</table>

				<hr color="#dde8f0" />

			</c:when>


			<c:when test="${navForm.tipoProspettazione eq 'consIndiceEsemplare'}">
				<table border="0" bgcolor="#FFCC99">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.consistenza.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="consistenzaPolo" cols="100" rows="4" readonly="true" ></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.denominazione.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.descrBiblioteca" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100"></html:text>
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.segnaturaAntica" cols="100" rows="2"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.consistenza" cols="100" rows="4"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.valoreM" style="width:40px">
							<html:optionsCollection property="listaMutiloM" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.note" cols="100" rows="3"></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.formatoElettronico" style="width:40px">
							<html:optionsCollection property="listaFormatoElettr" value="codice"
										label="descrizioneCodice" />
						</html:select></td>

						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:select property="localizzazione.tipoDigitalizzazione" style="width:40px">
							<html:optionsCollection property="listaTipoDigital" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100"></html:textarea>
						</td>
					</tr>
				</table>

				<hr color="#dde8f0" />

			</c:when>



			<c:when test="${navForm.tipoProspettazione eq 'modificaCollocazione'}">
				<table border="0" bgcolor="#FFCC99">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.consistenza.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="consistenzaPolo" cols="100" rows="4" readonly="true" ></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.denominazione.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.descrBiblioteca" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100"></html:text>
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.segnaturaAntica" cols="100" rows="2"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.consistenza" cols="100" rows="4"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.valoreM" style="width:40px">
							<html:optionsCollection property="listaMutiloM" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.note" cols="100" rows="3"></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.formatoElettronico" style="width:40px">
							<html:optionsCollection property="listaFormatoElettr" value="codice"
										label="descrizioneCodice" />
						</html:select></td>

						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:select property="localizzazione.tipoDigitalizzazione" style="width:40px">
							<html:optionsCollection property="listaTipoDigital" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100"></html:textarea>
						</td>
					</tr>
				</table>

				<hr color="#dde8f0" />

			</c:when>



			<c:when test="${navForm.tipoProspettazione eq 'modificaInventario'}">
				<table border="0" bgcolor="#FFCC99">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uri.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="uriPolo" rows="4" cols="100" readonly="true" ></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="descTipoDigitPolo" cols="100" readonly="true" ></html:textarea>
						</td>
					</tr>

				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.denominazione.daDocFis" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.descrBiblioteca" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100"></html:text>
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.segnaturaAntica" cols="100" rows="2"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.consistenza" cols="100" rows="4"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.valoreM" style="width:40px">
							<html:optionsCollection property="listaMutiloM" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea property="localizzazione.note" cols="100" rows="3"></html:textarea>
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.formatoElettronico" style="width:40px">
							<html:optionsCollection property="listaFormatoElettr" value="codice"
										label="descrizioneCodice" />
						</html:select></td>

						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:select property="localizzazione.tipoDigitalizzazione" style="width:40px">
							<html:optionsCollection property="listaTipoDigital" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100"></html:textarea>
						</td>
					</tr>
				</table>

				<hr color="#dde8f0" />

			</c:when>

			<c:otherwise>
				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.idAnagBibl" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.IDAnagrafe" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.codSbnBibl" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.IDSbn" size="100" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.fondo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.fondo" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnatura" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnatura" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.segnaturaAntica" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.segnaturaAntica" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="sintetica.consistenza" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.consistenza" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.note" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:text property="localizzazione.note" size="100"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.mutilo" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.valoreM" style="width:40px">
							<html:optionsCollection property="listaMutiloM" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.formatoElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
						<html:select property="localizzazione.formatoElettronico" style="width:40px">
							<html:optionsCollection property="listaFormatoElettr" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.tipoDigit" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:select property="localizzazione.tipoDigitalizzazione" style="width:40px">
							<html:optionsCollection property="listaTipoDigital" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" />:
						</td>
						<td class="testoNormale">
							<html:textarea rows="4" property="localizzazione.uriCopiaElettr" cols="100"></html:textarea>
						</td>
					</tr>

				</table>
			</c:otherwise>
		</c:choose>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${navForm.tipoProspettazione ne 'DET'}">
					<c:choose>
						<c:when test="${navForm.tipoProspettazione eq 'GESTCONS'}">
							<td><html:submit property="methodDettaglioLoc">
								<bean:message key="button.copia.consistenza"	bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:when test="${navForm.tipoProspettazione eq 'consIndiceEsemplare'}">
							<td><html:submit property="methodDettaglioLoc">
								<bean:message key="button.copia.consistenza"	bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:when test="${navForm.tipoProspettazione eq 'modificaCollocazione'}">
							<td><html:submit property="methodDettaglioLoc">
								<bean:message key="button.copia.consistenza"	bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:when test="${navForm.tipoProspettazione eq 'modificaInventario'}">
							<td><html:submit property="methodDettaglioLoc">
								<bean:message key="button.copia.consistenza"	bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>

					</c:choose>

						<td align="center"><html:submit property="methodDettaglioLoc">
							<bean:message key="button.gestLocal.confermaAgg"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</c:when>
				</c:choose>
				<td align="center"><html:submit property="methodDettaglioLoc">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>

			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
