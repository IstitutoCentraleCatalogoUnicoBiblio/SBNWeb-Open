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
	<sbn:navform action="/servizi/occupazioni/ListaOccupazioni">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />

			</div>
			<br>
			<c:if test="${not empty ListaOccupazioniForm.listaOccupazioni}">

				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
							elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
							parameter="methodListaOccup">
				</sbn:blocchi>
				<table class="sintetica">
					<tr>
						<th width="4%" class="etichetta" style="text-align:center;" scope="col" bgcolor="#dde8f0">
							n.
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							<bean:message key="servizi.utenti.headerBiblioteca"
								bundle="serviziLabels" />
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							<bean:message key="servizi.utenti.professione" bundle="serviziLabels" />
						</th>
						<th colspan=2 class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							<bean:message key="servizi.occupazioni.header.desOccup" bundle="serviziLabels" />
						</th>
						<!--
						<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							&nbsp;
						</th>
						-->
						<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							&nbsp;
						</th>
					</tr>
					<logic:iterate id="listaOccup" property="listaOccupazioni" name="ListaOccupazioniForm" indexId="indEle">
						<sbn:rowcolor var="color" index="indEle" />
						<tr  bgcolor="${color}" >
							<td class="testoNoBold" style="text-align:center;">
								<sbn:linkbutton index="idOccupazioni" name="listaOccup"
								value="progressivo" key="servizi.bottone.esaminaOne"
								bundle="serviziLabels" title="esamina" property="codSelOccupSing" />
							</td>
							<td class="testoNoBold" style="text-align:center;">
								<sbn:anchor name="listaOccup" property="progressivo" />
								<bean-struts:write name="listaOccup" property="codBiblioteca" />
							</td>

							<td class="testoNoBold" style="text-align:center;">
								<bean-struts:write name="listaOccup" property="descrizioneProfessione" />
							</td>
							<td class="testoNoBold" style="text-align:center;">
								<bean-struts:write name="listaOccup" property="codOccupazione" />
							</td>
							<td class="testoNoBold">
								<bean-struts:write name="listaOccup" property="desOccupazione" />
							</td>
							<!--
							<td class="testoNoBold" style="text-align:center;">
								<html:radio property="codSelOccupSing" value="${indEle}"
											style="text-align:center;"
											disabled="${ListaOccupazioniForm.conferma}" />
							</td>
							-->
							<td class="testoNoBold" style="text-align:center;">
								<html:multibox property="codSelOccup" value="${listaOccup.idOccupazioni}"
												style="text-align:center;"
												disabled="${ListaOccupazioniForm.conferma}" />
							</td>
						</tr>
					</logic:iterate>
				</table>
			</div>
			<div id="divFooterCommon">
				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
								elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
								parameter="methodListaMaterie" bottom="true" >
				</sbn:blocchi>
			</div>
			<br/>
			<br/>
		</c:if>

		<div id="divFooter">
			<c:choose>
				<c:when test="${ListaOccupazioniForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/occupazioni/footerListaOccupazioni.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>

