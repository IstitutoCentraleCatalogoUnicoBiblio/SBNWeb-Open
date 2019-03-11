<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<style>
input[type='radio'] {
	margin-right: 1em;
}
</style>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/documenti/RicercaDocumenti.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors bundle="serviziMessages" />
		</div>
		<br>
		<sbn:checkAttivita idControllo="CAMBIO_BIBLIOTECA">
			<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/listaBiblioteche.jsp" />
			<br>
		</sbn:checkAttivita>
		</div>
		<table>
		 	<tr>
				<td><bean:message key="servizi.documenti.tipoDoc" bundle="serviziLabels" /></td>
				<td><html:select property="filtro.tipo_doc_lett" disabled="${navForm.modalitaCerca eq 'CERCA_PER_EROGAZIONE'}">
					<html:optionsCollection property="listaTipoDocumento" label="descrizione" value="codice"/>
				</html:select></td>
				<td colspan="2">&nbsp;</td>
			</tr>

			<tr>
				<td><bean:message key="servizi.documenti.fonte" bundle="serviziLabels" /></td>
				<td><html:select property="filtro.fonte" disabled="${navForm.modalitaCerca eq 'CERCA_PER_EROGAZIONE'}">
					<html:optionsCollection property="listaTipoFonte" label="descrizione" value="codice"/>
				</html:select></td>
				<td>
					<sbn:checkAttivita idControllo="UTENTE">
						<bean:message key="servizi.documenti.utente" bundle="serviziLabels" />
					</sbn:checkAttivita>
				</td>
				<td>
					<sbn:checkAttivita idControllo="UTENTE">
						<html:text property="filtro.utente" maxlength="12" size="14" />
					</sbn:checkAttivita>
				</td>
			</tr>
			<sbn:checkAttivita idControllo="POSSEDUTO" inverted="true">
				<tr>
					<td><bean:message key="servizi.documenti.bid" bundle="serviziLabels" /></td>
					<td><html:text property="filtro.bid" maxlength="10" size="11" /></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><bean:message key="servizi.documenti.numeroStd"
							bundle="serviziLabels" /></td>
					<td><html:select style="width:100px" styleClass="testoNormale" property="filtro.tipoNumStd">
							<html:optionsCollection property="listaTipoNumeroStd"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select> &nbsp;<html:text styleId="testoNormale" property="filtro.numeroStd" size="25" maxlength="25" />
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</sbn:checkAttivita>
			<tr>
				<td><bean:message key="servizi.documenti.titolo" bundle="serviziLabels" /></td>
				<td><html:text property="filtro.titolo" maxlength="70" size="70" /></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><bean:message key="servizi.documenti.autore" bundle="serviziLabels" /></td>
				<td><html:text property="filtro.autore" maxlength="50" size="30" /></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><bean:message key="servizi.documenti.tipoRecord" bundle="serviziLabels" /></td>
				<td>
					<sbn:checkAttivita idControllo="POSSEDUTO">
						<html:select styleClass="testoNormale" property="filtro.tipoRecord">
							<html:optionsCollection property="listaTipoRecord" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="POSSEDUTO" inverted="true">
						<html:select styleClass="w10em" property="filtro.tipoRecord1">
							<html:optionsCollection property="listaTipoRecord" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
						<html:select styleClass="w10em" property="filtro.tipoRecord2">
							<html:optionsCollection property="listaTipoRecord" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
						<html:select styleClass="w10em" property="filtro.tipoRecord3">
							<html:optionsCollection property="listaTipoRecord" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</sbn:checkAttivita>
				</td>
				<td colspan="2">&nbsp;</td>
			</tr>

			<sbn:checkAttivita idControllo="POSSEDUTO">
				<tr>
					<td><bean:message key="servizi.documenti.segnatura" bundle="serviziLabels" /></td>
					<td><html:text property="filtro.segnatura" maxlength="40" size="30" /></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><bean:message key="servizi.documenti.inventario" bundle="serviziLabels" /></td>
					<td><html:text property="filtro.inventario" maxlength="12" size="15" /></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><bean:message key="servizi.documenti.dataDa" bundle="serviziLabels" /></td>
					<td><html:text property="filtro.dataInizio" maxlength="10" size="11" /></td>
					<td><bean:message key="servizi.documenti.dataA" bundle="serviziLabels" /></td>
					<td><html:text property="filtro.dataFine" maxlength="10" size="11" /></td>
				</tr>
			</sbn:checkAttivita>
			<sbn:checkAttivita idControllo="POSSEDUTO" inverted="true">
			<%--
				<tr>
					<td><bean:message key="servizi.documenti.tipoRecord" bundle="serviziLabels" /></td>
					<td>
						Tutti&nbsp;<html:radio property="filtro.tipoDocumento" value="TUTTI" />
						Testo&nbsp;<html:radio property="filtro.tipoDocumento" value="TESTO" />
						Musica&nbsp;<html:radio property="filtro.tipoDocumento" value="MUSICA" />
						Grafica&nbsp;<html:radio property="filtro.tipoDocumento" value="GRAFICA" />
						Cartografia&nbsp;<html:radio property="filtro.tipoDocumento" value="CARTOGRAFIA" />
						Audiovisivi&nbsp;<html:radio property="filtro.tipoDocumento" value="AUDIOVISIVI" />
						Altro&nbsp;<html:radio property="filtro.tipoDocumento" value="ALTRO" />
					</td>
				</tr>
			--%>
				<tr>
					<td>
						<bean:message key="servizi.documenti.dataDa" bundle="serviziLabels" />
					</td>
					<td>
						<html:text property="filtro.annoDa" maxlength="4" size="5" />
					&nbsp;
						<bean:message key="servizi.documenti.dataA" bundle="serviziLabels" />
						<html:text property="filtro.annoA" maxlength="4" size="5" />
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><bean:message key="servizi.documenti.natura"
							bundle="serviziLabels" /></td>
					<td><html:select styleClass="testoNormale"
							property="filtro.natura">
							<html:optionsCollection property="listaNature" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</sbn:checkAttivita>
		</table>

		<hr>
		<table>
			<tr>
				<td><bean:message key="servizi.label.elementiPerBlocco" bundle="serviziLabels" />:</td>
				<td><html:text property="filtro.elementiPerBlocco" maxlength="3" size="4" /></td>
				<td><bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />:</td>
				<td><html:select property="filtro.ordinamento">
					<html:optionsCollection property="listaTipoOrdinamento" label="descrizione" value="codice"/>
				</html:select></td>
			</tr>
		</table>
		<br>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="listaPulsanti" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
