<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/relazioni/GestioneRelazioni.do">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
		</div>
		<div><br />
		<div><bean:message key="servizi.relazioni.listaRelazioni"
			bundle="serviziLabels" />&nbsp;&nbsp;<html:select
			property="codiceRelazione"
			disabled="${GestioneRelazioniForm.step ne GestioneRelazioniForm.sceltaRelazione}">
			<html:optionsCollection property="lstRelazioni" value="codice"
				label="descrizione" />
		</html:select></div>
		<c:choose>
			<c:when
				test="${GestioneRelazioniForm.step eq GestioneRelazioniForm.gestioneRelazioni}">
				<c:choose>
					<c:when test="${empty GestioneRelazioniForm.lstRelazioniCaricate}">
						<c:if
							test="${GestioneRelazioniForm.codiceRelazione eq GestioneRelazioniForm.tipoServizioCategoriaFruizione}">
							<div><bean:message
								key="message.servizi.relazioni.tipoServizioCategoriaFruizione.nessunaRelazione"
								bundle="serviziMessages" /></div>
						</c:if>
						<c:if
							test="${GestioneRelazioniForm.codiceRelazione eq GestioneRelazioniForm.tipoServizioModalitaErogazione}">
							<div><bean:message
								key="message.servizi.relazioni.tipoServizioModalitaErogazione.nessunaRelazione"
								bundle="serviziMessages" /></div>
						</c:if>
						<c:if
							test="${GestioneRelazioniForm.codiceRelazione eq GestioneRelazioniForm.tipoSupportoModalitaErogazione}">
							<div><bean:message
								key="message.servizi.relazioni.tipoSupportoModalitaErogazione.nessunaRelazione"
								bundle="serviziMessages" /></div>
						</c:if>
					</c:when>
					<c:otherwise>
						<br />
						<table class="sintetica">
							<tr bgcolor="#DDE8F0">
								<th width="4%" class="etichetta" scope="col">n.</th>
								<th width="4%" class="etichetta" scope="col"><bean:message
									key="servizi.relazioni.bib" bundle="serviziLabels" /></th>
								<th class="etichetta" scope="col"><bean:message
									key="servizi.relazioni.relazione1" bundle="serviziLabels" /></th>
								<th class="etichetta" scope="col"><bean:message
									key="servizi.relazioni.relazione2" bundle="serviziLabels" /></th>
								<th width="3%" class="etichetta" scope="col"><bean:message
									key="servizi.relazioni.attivo" bundle="serviziLabels" /></th>
								<th width="2%" class="etichetta" scope="col"><bean:message
									key="servizi.utenti.headerSelezionataMultipla"
									bundle="serviziLabels" /></th>
							</tr>
							<logic:iterate id="item" property="lstRelazioniCaricate"
								name="GestioneRelazioniForm" indexId="riga">
								<sbn:rowcolor var="color" index="riga" />
								<tr bgcolor="${color}">
									<td class="testoNoBold"><bean-struts:write name="item"
										property="progr" /></td>
									<td class="testoNoBold"><bean-struts:write name="item"
										property="codBib" /></td>
									<td class="testoNoBold"><bean-struts:write name="item"
										property="descIdTabellaRelazione" /></td>
									<td class="testoNoBold"><bean-struts:write name="item"
										property="descIdTabellaCodici" /></td>
									<td class="testoNoBold" align="center"><c:choose>
										<c:when test="${item.flCanc eq 'N'}">
											<bean:message key="servizi.relazioni.attivo.si"
												bundle="serviziLabels" />
										</c:when>
										<c:otherwise>
											<bean:message key="servizi.relazioni.attivo.no"
												bundle="serviziLabels" />
										</c:otherwise>
									</c:choose></td>
									<td class="testoNoBold"><html:multibox
										property="idSelMultipla" value="${item.id}"
										disabled="${GestioneRelazioniForm.conferma}" /></td>
								</tr>
							</logic:iterate>
						</table>
					</c:otherwise>
				</c:choose>
			</c:when>

			<%-- Qui di seguito le liste per la scelta degli elementi da associare --%>
			<c:when
				test="${GestioneRelazioniForm.step eq GestioneRelazioniForm.aggiungiModificaRelazione}">
				<br />
				<c:if
					test="${GestioneRelazioniForm.codiceRelazione eq GestioneRelazioniForm.tipoServizioCategoriaFruizione}">
					<table>
						<tr>
							<td><bean:message key="servizi.relazioni.tipoServ"
								bundle="serviziLabels" /></td>
							<td><bean:message key="servizi.relazioni.catFrui"
								bundle="serviziLabels" /></td>
						</tr>
						<tr>
							<td><html:select name="GestioneRelazioniForm"
								property="idTipoServizio">
								<html:optionsCollection property="lstTipiServizio"
									value="idTipoServizio" label="descrizione" />
							</html:select></td>
							<td><html:select name="GestioneRelazioniForm"
								property="codFruizione">
								<html:optionsCollection property="lstCategorieFruizione"
									value="codice" label="descrizione" />
							</html:select></td>
						</tr>
					</table>
				</c:if>
				<c:if
					test="${GestioneRelazioniForm.codiceRelazione eq GestioneRelazioniForm.tipoServizioModalitaErogazione}">
					<table>
						<tr>
							<td><bean:message key="servizi.relazioni.tipoServ"
								bundle="serviziLabels" /></td>
							<td><bean:message key="servizi.relazioni.modErog"
								bundle="serviziLabels" /></td>
						</tr>
						<tr>
							<td><html:select name="GestioneRelazioniForm"
								property="idTipoServizio">
								<html:optionsCollection property="lstTipiServizio"
									value="idTipoServizio" label="descrizione" />
							</html:select></td>
							<td><html:select name="GestioneRelazioniForm"
								property="codModalitaErogazione">
								<html:optionsCollection property="lstModalitaErogazione"
									value="codice" label="descrizione" />
							</html:select></td>
						</tr>
					</table>
				</c:if>
				<c:if
					test="${GestioneRelazioniForm.codiceRelazione eq GestioneRelazioniForm.tipoSupportoModalitaErogazione}">
					<table>
						<tr>
							<td><bean:message key="servizi.relazioni.tipoSupp"
								bundle="serviziLabels" /></td>
							<td><bean:message key="servizi.relazioni.modErog"
								bundle="serviziLabels" /></td>
						</tr>
						<tr>
							<td><html:select name="GestioneRelazioniForm"
								property="idTipoSupporto">
								<html:optionsCollection property="lstTipiSupporto"
									value="id" label="descrizione" />
							</html:select></td>
							<td><html:select name="GestioneRelazioniForm"
								property="codModalitaErogazione">
								<html:optionsCollection property="lstModalitaErogazione"
									value="codice" label="descrizione" />
							</html:select></td>
						</tr>
					</table>
				</c:if>
			</c:when>
		</c:choose></div>

		<br />
		<br />
		<div id="divFooter"><c:choose>
			<c:when test="${GestioneRelazioniForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<c:if
					test="${GestioneRelazioniForm.step eq GestioneRelazioniForm.sceltaRelazione}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/relazioni/footerScegliRelazione.jsp" />
				</c:if>
				<c:if
					test="${GestioneRelazioniForm.step eq GestioneRelazioniForm.gestioneRelazioni}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/relazioni/footerGestioneRelazioni.jsp" />
				</c:if>
				<c:if
					test="${GestioneRelazioniForm.step eq GestioneRelazioniForm.aggiungiModificaRelazione}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/relazioni/footerAggiungiModificaRelazione.jsp" />
				</c:if>
			</c:otherwise>
		</c:choose></div>


		</div>
	</sbn:navform>
</layout:page>
