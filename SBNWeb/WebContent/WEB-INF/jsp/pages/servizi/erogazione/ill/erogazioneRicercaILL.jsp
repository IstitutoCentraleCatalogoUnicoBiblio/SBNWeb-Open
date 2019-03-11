<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/erogazione/erogazioneRicercaILL.do">

		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<br>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utility/listaBiblioteche.jsp" />
			<br/>

			<div>
				<!-- N. RICHIESTA ILL -->
				<div>
					<div style="float: left; width: 130px">
						<bean:message key="servizi.erogazione.codRichILL" bundle="serviziLabels" />
					</div>
					<div style="float: none;">
						<html:text property="ricerca.transactionId" maxlength="9" size="10" />
					</div>
					<br />
				</div>
				<!-- STATO RICHIESTA -->
				<div>
					<div style="float: left; width: 130px">
						<bean:message key="servizi.erogazione.ill.statoAttivitaILL"
							bundle="serviziLabels" />
					</div>
					<div style="float: none;">
						<html:select property="ricerca.currentState">
							<html:optionsCollection property="listaStatoRichiestaILL"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</div>
					<br />
				</div>
				<!-- STATO RICHIESTA LOCALE -->
				<div>
					<div style="float: left; width: 130px">
						<bean:message key="servizi.erogazione.ill.statoAttivitaLoc"
							bundle="serviziLabels" />
					</div>
					<div style="float: none;">
						<html:select property="ricerca.codStatoRic">
							<html:optionsCollection property="listaStatoRichiestaLocale"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</div>
					<br />
				</div>

				<c:if test="${navForm.folder eq 'RICHIEDENTE'}">
					<!-- BIB. FORNITRICE -->
					<div>
						<div style="float: left; width: 130px">
							<bean:message key="servizi.erogazione.ill.bib.fornitrice" bundle="serviziLabels"/>
						</div>
						<div style="float: none;">
							<html:text styleId="isil" property="bibliotecaFornitrice.cd_ana_biblioteca" size="8" readonly="true" />&nbsp;
							<html:text styleId="nomeBib" property="bibliotecaFornitrice.nom_biblioteca" size="50" readonly="true" /> &nbsp;
							<html:submit styleClass="buttonImage" property="methodRicercaILL">
								<bean:message key="servizi.bottone.biblioteca" bundle="serviziLabels" />
							</html:submit>
							<input type="button" onclick="pulisciBib()" class="buttonDelete"
								title='<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />' />
						</div>
						<br />
					</div>
					<!-- UTENTE LOCALE -->
					<div>
						<div style="float: left; width: 130px">
							<bean:message key="servizi.erog.Utente" bundle="serviziLabels" />
						</div>
						<div style="float: none;">
							<html:text styleId="testoNoBold" property="ricerca.codUtente"
								titleKey="servizi.erogazione.codiceUtente" bundle="serviziLabels" size="30" maxlength="25" />&nbsp;&nbsp;&nbsp;
							<html:submit styleClass="buttonImage" property="methodRicercaILL" titleKey="servizi.erogazione.ricercaUtente" bundle="serviziLabels">
								<bean:message key="servizi.bottone.hlputente" bundle="serviziLabels" />
							</html:submit>
						</div>
						<br />
					</div>
				</c:if>
			</div>
			<br />

			<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/erogazione/ill/folderErogazioneRicercaILL.jsp" />
				<br />

				<sbn:blocchi numBlocco="bloccoSelezionato"
					numNotizie="blocco.totRighe" parameter="methodRicercaILL"
					totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe"
					disabled="${navForm.conferma}" />

				<c:choose>
					<c:when test="${navForm.folder eq 'RICHIEDENTE'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/servizi/erogazione/ill/listaRichiesteILL_requester.jsp" />
					</c:when>
					<c:when test="${navForm.folder eq 'FORNITRICE'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/servizi/erogazione/ill/listaRichiesteILL_responder.jsp" />
					</c:when>
				</c:choose>

		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="bloccoSelezionato"
				numNotizie="blocco.totRighe" parameter="methodRicercaILL"
				totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe"
				bottom="true" disabled="${navForm.conferma}" />
			<table border="0">
				<tr>
					<td class="etichetta">
						<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
					</td>
					<td  class="testoNormale">
						<html:select property="ricerca.tipoOrdinamento">
							<html:optionsCollection property="listaTipoOrdinamento" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>

</layout:page>
<script type="text/javascript" src='<c:url value="/scripts/servizi/ill/ricerca.js" />'></script>

