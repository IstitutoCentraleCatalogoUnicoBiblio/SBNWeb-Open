<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/spectitolostudio/ListaSpecTitoloStudio">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />

			</div>
			<br/>
			<c:if test="${not empty ListaSpecTitoloStudioForm.listaSpecialita}">

				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
							elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
							parameter="methodListaSpecialita">
				</sbn:blocchi>
				<table class="sintetica">
					<tr>
						<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
							n.
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							<bean:message key="servizi.utenti.headerBiblioteca" bundle="serviziLabels" />
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							<bean:message key="servizi.utenti.titoloStudio" bundle="serviziLabels" />
						</th>
						<th colspan=2 class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
							<bean:message key="servizi.utenti.specTitoloStudio" bundle="serviziLabels" />
						</th>
						<!--
						<th scope="col" bgcolor="#dde8f0">
							&nbsp;
						</th>
						-->
						<th scope="col" bgcolor="#dde8f0">
							&nbsp;
						</th>
					</tr>
					<logic:iterate id="listaSpec" property="listaSpecialita" name="ListaSpecTitoloStudioForm" indexId="indEle">
						<sbn:rowcolor var="color" index="indEle" />

						<tr bgcolor="${color}">
							<td class="testoNoBold">
								<sbn:linkbutton index="idTitoloStudio" name="listaSpec"
									value="progressivo" key="servizi.bottone.esaminaOne"
									bundle="serviziLabels" title="esamina" property="codSelSpecialitaSing" />
							</td>

							<td class="testoNoBold" style="text-align:center;">
								<sbn:anchor name="listaSpec" property="progressivo" />
								<bean-struts:write name="listaSpec" property="codBiblioteca" />
							</td>
							<td class="testoNoBold" style="text-align:center;">
								<bean-struts:write name="listaSpec" property="descrizioneTitoloStudio" />
							</td>
							<td class="testoNoBold" style="text-align:center;">
								<bean-struts:write name="listaSpec" property="codSpecialita" />
							</td>
							<td class="testoNoBold">
								<bean-struts:write name="listaSpec" property="desSpecialita" />
							</td>
							<!--
							<td class="testoNoBold" style="text-align:center;">
								<html:radio property="codSelSpecialitaSing" value="${indEle}"
											style="text-align:center;"
											disabled="${ListaSpecTitoloStudioForm.conferma}" />
							</td>
							-->
							<td class="testoNoBold" style="text-align:center;">
								<html:multibox property="codSelSpecialita" value="${listaSpec.idTitoloStudio}"
												style="text-align:center;"
												disabled="${ListaSpecTitoloStudioForm.conferma}" />
							</td>
						</tr>
					</logic:iterate>
				</table>
			</div>
			<div id="divFooterCommon">
				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
								elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
								parameter="methodListaSpecialita" bottom="true" >
				</sbn:blocchi>
			</div>
			<br/>
			<br/>
		</c:if>

		<div id="divFooter">
			<c:choose>
				<c:when test="${ListaSpecTitoloStudioForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/spectitolostudio/footerListaTitoloStudio.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>

