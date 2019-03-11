<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/occupazioni/DettaglioOccupazioni">
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
						<html:text styleId="testoNoBold"
									property="dettaglio.codBiblioteca" size="5"
									disabled="true">
						</html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.utenti.professione" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:select property="dettaglio.professione"
							disabled="${DettaglioOccupazioniForm.conferma or !DettaglioOccupazioniForm.dettaglio.newOccupazione}">
							<html:optionsCollection property="lstProfessioni"
													value="codice"
													label="descrizioneCodice" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.occupazioni.header.codOccup" bundle="serviziLabels" />
					</td>
					<td>
						<html:text styleId="testoNoBold"
									property="dettaglio.codOccupazione" size="2" maxlength="2"
									disabled="${DettaglioOccupazioniForm.conferma or !DettaglioOccupazioniForm.dettaglio.newOccupazione}"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.occupazioni.header.desOccup" bundle="serviziLabels" />
					</td>
					<td>
						<html:text styleId="testoNoBold"
									property="dettaglio.desOccupazione" size="50"  maxlength="50"
									disabled="${DettaglioOccupazioniForm.conferma}"></html:text>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<c:choose>
				<c:when test="${DettaglioOccupazioniForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/occupazioni/footerDettOccupazioni.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
