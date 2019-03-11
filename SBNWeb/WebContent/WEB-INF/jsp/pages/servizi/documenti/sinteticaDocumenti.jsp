<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/servizi/documenti/ListaDocumenti.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<br>
			<sbn:blocchi numBlocco="bloccoSelezionato"
				numNotizie="blocco.totRighe" parameter="methodListaDoc"
				totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe"
				disabled="${navForm.conferma}" />

			<sbn:disableAll disabled="${navForm.conferma}">
			<table class="sintetica">
				<c:choose>
				<c:when test="${navForm.filtro.ricercaOpac}">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">n.</th>
					<!--
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.tipoDoc"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.codDoc"
							bundle="serviziLabels" />
					</th>
					-->
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.bid" bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.titolo"
							bundle="serviziLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.natura"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.autore"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.editore"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.luogo"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.anno"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.numBib"
							bundle="serviziLabels" />
					</th>

					<!-- <th class="etichetta" scope="col" bgcolor="#dde8f0"></th>-->
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
					<l:iterate id="item" property="documenti" name="navForm"
						indexId="progr">
						<sbn:rowcolor var="color" index="progr" />
						<tr bgcolor="${color}">
							<td class="testoNoBold"><sbn:anchor name="item"
									property="progr" /> <sbn:linkbutton index="uniqueId"
									name="item" value="progr" key="servizi.bottone.esaminaOne"
									bundle="serviziLabels" title="esamina"
									property="codSelezionato" /></td>
							<!--
							<td class="testoNoBold"><bs:write name="item"
									property="tipo_doc_lett" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="cod_doc_lett" /></td>
 						    -->
							<td class="testoNoBold"><bs:write name="item"
									property="bid" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="titolo" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="natura" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="autore" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="editore" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="luogoEdizione" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="annoEdizione" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="numBiblioteche" /></td>
							<td class="testoNoBold"><sbn:checkAttivita
									idControllo="SELEZIONE_SINGOLA">
									<html:radio property="codSelezionato" value="${item.uniqueId}" />
								</sbn:checkAttivita> <sbn:checkAttivita idControllo="SELEZIONE_SINGOLA"
									inverted="true">
									<html:multibox property="idSelezionati"
										value="${item.uniqueId}" />
									<html:hidden property="idSelezionati" value="0" />
								</sbn:checkAttivita></td>
						</tr>
					</l:iterate>

				</c:when>
				<c:otherwise>
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">n.</th>
					<c:if test="${navForm.modalitaCerca eq 'CERCA_PER_EROGAZIONE'}">
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="servizi.documenti.codBib"
								bundle="serviziLabels" />
						</th>
					</c:if>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.tipoDoc"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.codDoc"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.fonte" bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.titolo"
							bundle="serviziLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.natura"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.codFrui"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.codNoDisp"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.segnatura"
							bundle="serviziLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.numCopie"
							bundle="serviziLabels" />
					</th>
					<!--				<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>-->
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
					<l:iterate id="item" property="documenti" name="navForm"
						indexId="progr">
						<sbn:rowcolor var="color" index="progr" />
						<tr bgcolor="${color}">
							<td class="testoNoBold"><sbn:anchor name="item"
									property="progr" /> <sbn:linkbutton index="uniqueId"
									name="item" value="progr" key="servizi.bottone.esaminaOne"
									bundle="serviziLabels" title="esamina"
									property="codSelezionato" /></td>
							<c:if test="${navForm.modalitaCerca eq 'CERCA_PER_EROGAZIONE'}">
								<td class="testoNoBold"><bs:write name="item"
										property="codBib" /></td>
							</c:if>
							<td class="testoNoBold"><bs:write name="item"
									property="tipo_doc_lett" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="cod_doc_lett" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="fonte" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="titolo" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="natura" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="categoriaFruizione" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="nonDisponibile" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="segnatura" /></td>
							<td class="testoNoBold"><bs:write name="item"
									property="numeroEsemplari" /></td>
							<td class="testoNoBold"><sbn:checkAttivita
									idControllo="SELEZIONE_SINGOLA">
									<html:radio property="codSelezionato" value="${item.uniqueId}" />
								</sbn:checkAttivita> <sbn:checkAttivita idControllo="SELEZIONE_SINGOLA"
									inverted="true">
									<html:multibox property="idSelezionati"
										value="${item.uniqueId}" />
									<html:hidden property="idSelezionati" value="0" />
								</sbn:checkAttivita></td>
						</tr>
					</l:iterate>

				</c:otherwise>
				</c:choose>
			</table>
			</sbn:disableAll>
		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="bloccoSelezionato"
				numNotizie="blocco.totRighe" parameter="methodListaDoc"
				totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe"
				bottom="true" disabled="${navForm.conferma}" />
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="listaPulsanti" /></td>
					<td><c:if
							test="${not empty navForm.documenti and navForm.modalitaCerca ne 'CERCA_PER_EROGAZIONE' and not navForm.conferma}">
							<html:submit property="methodListaDoc"
								styleClass="buttonSelezTutti"
								titleKey="servizi.title.selezionaTutti" bundle="serviziLabels">
								<bean:message key="servizi.bottone.selTutti"
									bundle="serviziLabels" />
							</html:submit>
							<html:submit property="methodListaDoc"
								styleClass="buttonSelezNessuno"
								titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels">
								<bean:message key="servizi.bottone.deselTutti"
									bundle="serviziLabels" />
							</html:submit>
						</c:if></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>

