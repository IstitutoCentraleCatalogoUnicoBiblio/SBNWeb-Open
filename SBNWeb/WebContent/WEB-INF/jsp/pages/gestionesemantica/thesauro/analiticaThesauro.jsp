<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />

<layout:page>
	<sbn:navform action="/gestionesemantica/thesauro/AnaliticaThesauro.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0">
			<tr>
				<td><bean:message key="label.livRicercaPolo"
					bundle="gestioneBibliograficaLabels" /> <c:if
					test="${AnaliticaThesauroForm.enableMultiAnalitica}">
					<html:submit property="methodAnaThes">
						<bean:message key="button.elemPrec"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
					<html:submit property="methodAnaThes">
						<bean:message key="button.elemSucc"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
				</c:if></td>
			</tr>
		</table>
		<br>
		<table>
			<tr>
				<td class="etichetta"><bean:message
					key="gestionesemantica.thesauro.thesauro"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="reticolo.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO.codThesauro"
					disabled="true">
					<html:optionsCollection property="listaThesauri" value="codice"
						label="descrizione" />
				</html:select></td>
				<td class="etichetta"><bean:message key="analitica.stato"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="reticolo.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO.livAut"
					disabled="true">
					<html:optionsCollection property="listaLivelloAutorita"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		<br>
		<sbn:tree root="reticolo" divClass="analitica" visualCheck="false"
			propertyRadio="nodoSelezionato" propertyCheck="checkSelezionato"
			imagesPath="/sbn/images/tree/" enableSubmit="true"
			enabled="${!AnaliticaThesauroForm.enableConferma}" />
		<br>
		<table width="100%" border="0">
			<tr>
				<td align="left" class="etichetta" scope="col"><bean:message
					key="analitica.inserito" bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="analitica.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="reticolo.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO.dataIns"
					size="14" maxlength="20" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="analitica.modificato"
					bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="analitica.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="reticolo.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO.dataAgg"
					size="14" maxlength="20" readonly="true"></html:text></td>
			</tr>

		</table>
		<!-- Titoli collegati -->
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta" scope="col"><bean:message
					key="analitica.thesauro.titoli" bundle="gestioneSemanticaLabels" />
				</td>
				<td align="center" class="etichetta" scope="col"><bean:message
					key="analitica.polo" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="reticolo.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO.numTitoliPolo"
					size="10" readonly="true"></html:text></td>
				<td align="center" class="etichetta" scope="col"><bean:message
					key="analitica.biblio" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="reticolo.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO.numTitoliBiblio"
					size="10" readonly="true"></html:text></td>
			</tr>
		</table>
		</div>
		<div id="divFooter"><c:choose>
			<c:when test="${AnaliticaThesauroForm.enableConferma}">
				<table align="center">
					<tr>
						<td align="center"><sbn:bottoniera buttons="listaPulsanti" />
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/thesauro/bottonieraAnaliticaThes.jsp" />
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
