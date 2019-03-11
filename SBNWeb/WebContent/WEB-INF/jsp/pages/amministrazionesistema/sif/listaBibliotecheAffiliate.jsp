<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform
		action="/amministrazionesistema/sif/listaBibliotecheAffiliate.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors	bundle="amministrazioneSistemaMessages" />
		</div>

		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodListaBibAff" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" />

		<table class=sintetica>
			<tr class="etichetta" bgcolor="#dde8f0">
				<th style="width: 8%"><bean:message
					key="documentofisico.codice" bundle="documentoFisicoLabels" /></th>
				<th><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></th>
				<th style="width: 3%">
				<div align="center"></div>
				</th>
			</tr>
			<logic:iterate id="item" property="listaBiblioteche"
				name="navForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td>
						<sbn:anchor name="item" property="prg"/>
						<c:choose>
							<c:when test="${navForm.attivazioneSIF.multiSelezione}">
								<sbn:linkbutton name="item" index="repeatableId" value="cod_bib"
									property="multiBiblio" key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
							</c:when>
							<c:otherwise>
								<sbn:linkbutton name="item" index="repeatableId" value="cod_bib"
									property="selectedBiblio" key="documentofisico.bottone.scegli" bundle="documentoFisicoLabels" />
							</c:otherwise>
						</c:choose>
						<!-- <bean-struts:write name="item" property="cod_bib" /> -->
					</td>
					<td>
						<bean-struts:write name="item" property="nom_biblioteca" />
					</td>
					<td>
						<c:choose>
							<c:when test="${navForm.attivazioneSIF.multiSelezione}">
								<html:multibox property="multiBiblio" value="${item.repeatableId}" />
								<html:hidden property="multiBiblio" value="0" />
							</c:when>
							<c:otherwise>
								<html:radio property="selectedBiblio" value="${item.repeatableId}" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodListaBibAff" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi> <br />
		</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<td><html:submit styleClass="pulsanti"
					property="methodListaBibAff">
					<bean:message key="documentofisico.bottone.scegli"
						bundle="documentoFisicoLabels" />
				</html:submit> <html:submit styleClass="pulsanti" property="methodListaBibAff">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
				<c:if test="${navForm.attivazioneSIF.multiSelezione}">
					<td align="right">
						<html:submit styleClass="buttonSelezTutti"
							property="methodListaBibAff" title="Seleziona tutto">
							<bean:message key="button.selAllTitoli"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
						<html:submit styleClass="buttonSelezNessuno"
							property="methodListaBibAff" title="Deseleziona tutto">
							<bean:message key="button.deSelAllTitoli"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
				</c:if>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>