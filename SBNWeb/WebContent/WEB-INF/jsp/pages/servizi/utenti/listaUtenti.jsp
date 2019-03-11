<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/utenti/ListaUtenti">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
		</div>
		<c:choose>
			<c:when test="${navForm.richiesta eq 'RinnovaAut'}">
				<table width="100%" border="0">
					<tr>
						<td class="etichetta" align="right"><bean:message
							key="servizi.utenti.dataRinnovoAut" bundle="serviziLabels" /></td>
						<td><html:text styleId="testoNoBold"
							property="dataRinnovoAut" size="10"
							disabled="${!navForm.conferma}"></html:text></td>
					</tr>
				</table>
				<br />
			</c:when>
			<c:otherwise>

			</c:otherwise>
		</c:choose> <sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
			elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
			parameter="methodLista"
			livelloRicerca="${navForm.livelloRicerca}">
		</sbn:blocchi>

		<table class="sintetica">
			<tr>
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerProgressivo"
					bundle="serviziLabels" /></th>
				<!--
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerPolo" bundle="serviziLabels" />
				</th>
				<th width="6%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerBiblioteca"
					bundle="serviziLabels" /></th>
				-->
				<th width="6%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerCodUtente"
					bundle="serviziLabels" /></th>
			<c:choose>
				<c:when test="${!navForm.listaUtentiPolo}">
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerCodAutorizzazione"
					bundle="serviziLabels" /></th>
				</c:when>
			</c:choose>

				<th width="31%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerCognomeNome"
					bundle="serviziLabels" /></th>
				<!--
				<th width="25%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.headerIndirizzo"
					bundle="serviziLabels" /></th>
				-->
				<th width="25%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.luogoNascita"
					bundle="serviziLabels" /></th>
				<th width="25%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.residenzaSede"
					bundle="serviziLabels" /></th>
				<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0">
				<bean:message key="servizi.utenti.dataNascitaBreve"
					bundle="serviziLabels" /></th>
				<!--
				<th width="2%" class="etichetta" scope="col" bgcolor="#dde8f0">
				&nbsp;</th>
				-->
					<th width="2%" class="etichetta" scope="col" bgcolor="#dde8f0">
					&nbsp;</th>
			</tr>
			<c:choose>
				<c:when test="${navForm.numUtenti gt 0}">
				<l:iterate id="item" property="listaUtenti"
					name="navForm" indexId="indEle">
					<sbn:rowcolor var="color" index="indEle" />

					<tr>
						<td bgcolor="${color}" class="testoNoBold">
						<sbn:anchor name="item" property="progressivo" />
						<sbn:linkbutton
							index="idUtente" name="item" value="progressivo"
							key="servizi.bottone.esamina" bundle="serviziLabels"
							title="Scegli" property="codUtente"
							disabled="${navForm.listaUtentiPolo}"
							withAnchor="false" /></td>
						<!--
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="polo" /></td>
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="biblioDiUteBiblio" /></td>
						-->
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="codice" /></td>

				<c:choose>
					<c:when test="${!navForm.listaUtentiPolo}">
						<td bgcolor="${color}" class="testoNoBold" align="center">
	<!--						<bs:write name="item" property="flgAutorizzazione" />-->
							<bs:write name="item" property="codiceAutorizzazione" />
						</td>
					</c:when>
				</c:choose>

						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="descrizione" /></td>
						<!--
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="indirizzo" /></td>
						-->
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="luogoNascita" /></td>
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="cittaRes" /></td>
						<td bgcolor="${color}" class="testoNoBold"><bs:write
							name="item" property="dataNascita" /></td>
						<!--
						<td bgcolor="${color}" class="testoNoBold"><html:radio
							property="codUtenteSing" value="${item.idUtente}"
							disabled="false" titleKey="servizi.erogazione.sinteticaUtenti.selezioneSingola" bundle="serviziLabels" /></td>
						-->
							<td bgcolor="${color}" class="testoNoBold"><html:multibox
								property="codUtente" value="${item.idUtente}"
								disabled="${navForm.conferma}" titleKey="servizi.erogazione.sinteticaUtenti.selezioneMultipla" bundle="serviziLabels" /></td>
					</tr>
				</l:iterate>
				</c:when>
			</c:choose>

		</table>
		</div>

		<div id="divFooterCommon">
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
			elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
			parameter="methodLista" bottom="true"></sbn:blocchi>
		</div>

		<div id="divFooter">
		<c:choose>
			<c:when test="${navForm.richiesta eq 'ErogazioneRicerca' or navForm.richiesta eq 'ListaMovimenti' or navForm.richiesta eq 'SuggLett'}">
				<table align="center">
					<tr>
						<td><sbn:bottoniera buttons="pulsanti" /></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${navForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</c:when>
			<c:when test="${navForm.listaUtentiPolo}">
				<table align="center">
				<tr>
					<td align="center">
					<c:choose>
						<c:when test="${navForm.numUtenti gt 0}">
							<sbn:checkAttivita idControllo="GESTIONE">
								<html:submit property="methodLista">
									<bean:message key="servizi.bottone.importaUtente" bundle="serviziLabels" />
								</html:submit>
							</sbn:checkAttivita>
							<html:submit property="methodLista">
								<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
							</html:submit>
							<sbn:checkAttivita idControllo="GESTIONE">
								<html:submit property="methodLista">
									<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
								</html:submit>
							</sbn:checkAttivita>
					</c:when>
					<c:otherwise>
							<html:submit property="methodLista">
								<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
							</html:submit>
					</c:otherwise>
					</c:choose>

					</td>
				</tr>
				</table>
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utenti/footerListaUtenti.jsp" />
			</c:otherwise>
		</c:choose>
		</div>
	</sbn:navform>
</layout:page>
