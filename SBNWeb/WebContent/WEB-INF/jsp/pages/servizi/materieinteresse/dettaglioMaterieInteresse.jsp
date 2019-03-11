<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"      prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	 prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/materieinteresse/DettaglioMaterieInteresse">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />

			</div>
			<br>
			<table width="100%" border="0">
				<tr>
					<td width="24%" align="right" class="etichetta">
						<bean:message key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="anaMateria.codBib" size="5"
									disabled="true"></html:text>
					</td>
				</tr>
				<tr>
					<td width="24%" align="right" class="etichetta">
						<bean:message key="servizi.materieinteresse.header.codMateria" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="anaMateria.codice" size="3" maxlength="3"
									disabled="${DettaglioMaterieInteresseForm.conferma or !DettaglioMaterieInteresseForm.nuovo}"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.materieinteresse.header.desMateria" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="anaMateria.descrizione" size="30" maxlength="30"
									disabled="${DettaglioMaterieInteresseForm.conferma}"></html:text>
					</td>
				</tr>
			</table>
		</div>
		<br/>

		<div id="divFooter">
			<c:choose>
				<c:when test="${DettaglioMaterieInteresseForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/materieinteresse/footerDettMaterieInteresse.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
