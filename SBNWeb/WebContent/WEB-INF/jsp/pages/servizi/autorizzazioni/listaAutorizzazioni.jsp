<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/autorizzazioni/ListaAutorizzazioni">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
				elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
				parameter="methodListaAnagAut">
			</sbn:blocchi>
			<table class="sintetica">
				<tr>
					<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
						n.
					</th>
					<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="servizi.autorizzazioni.header.bib"
							bundle="serviziLabels" />
					</th>
					<th width="6%" class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="servizi.autorizzazioni.header.codAut"
							bundle="serviziLabels" />
					</th>
					<th width="31%" class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="servizi.autorizzazioni.desAutorizzazione"
							bundle="serviziLabels" />
					</th>
					<th width="25%" class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="servizi.autorizzazioni.header.automaticaX"
							bundle="serviziLabels" />
					</th>
					<th width="5%" class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="servizi.utenti.headerSelezionataMultipla"
							bundle="serviziLabels" />
					</th>
				</tr>
				<logic:iterate id="listaAut" property="listaAutorizzazioni"
					name="ListaAutorizzazioniForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr bgcolor="${color}">
						<td class="testoNoBold">
							<sbn:linkbutton index="idAutorizzazione" name="listaAut"
								value="progressivo" key="servizi.bottone.esaminaOne"
								bundle="serviziLabels" title="esamina" property="codSelAutSing" />
						</td>
						<td class="testoNoBold">
							<bean-struts:write name="listaAut" property="codBiblioteca" />
						</td>
						<td class="testoNoBold">
							<bean-struts:write name="listaAut" property="codAutorizzazione" />
						</td>
						<td class="testoNoBold">
							<bean-struts:write name="listaAut" property="desAutorizzazione" />
						</td>
						<td class="testoNoBold">
							<bean-struts:write name="listaAut" property="automaticoPer" />
						</td>
						<td class="testoNoBold">
							<html:multibox property="codSelAut" value="${listaAut.idAutorizzazione}"
								disabled="${ListaAutorizzazioniForm.conferma}" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
				elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
				parameter="methodListaAnagAut" bottom="true" />
		</div>
		<div id="divFooter">
			<c:choose>
				<c:when test="${ListaAutorizzazioniForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/autorizzazioni/footerListaAutorizzazioni.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>