<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/spectitolostudio/DettaglioSpecTitoloStudio">
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
					<html:text styleId="testoNoBold" property="anaSpecialita.codBiblioteca" size="5" disabled="true"></html:text>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.spectitolostudio.header.titoloStudio" bundle="serviziLabels" />
				</td>
				<c:choose>
	    			<c:when test="${DettaglioOccupazioniForm.conferma}">
						<td align="left">
							<html:select property="anaSpecialita.titoloStudio" disabled="${DettaglioSpecTitoloStudioForm.conferma}">
								<html:optionsCollection property="listaTdS" value="codice" label="descrizioneCodice" />
			 				</html:select>
						</td>
		    		</c:when>
					<c:otherwise>
						<td align="left">
							<html:select property="anaSpecialita.titoloStudio" disabled="${!DettaglioSpecTitoloStudioForm.anaSpecialita.newSpecialita}">
								<html:optionsCollection property="listaTdS" value="codice" label="descrizioneCodice" />
			 				</html:select>
						</td>
    				</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.spectitolostudio.header.codSpecTitoloStudio" bundle="serviziLabels" />
				</td>
				<c:choose>
	    			<c:when test="${DettaglioOccupazioniForm.conferma}">
						<td align="left">
							<html:text styleId="testoNoBold" property="anaSpecialita.codSpecialita"
										maxlength="1"
										disabled="${DettaglioSpecTitoloStudioForm.conferma}"></html:text>
						</td>
		    		</c:when>
					<c:otherwise>
						<td align="left">
							<html:text styleId="testoNoBold" property="anaSpecialita.codSpecialita"
										size="2" maxlength="2"
										disabled="${!DettaglioSpecTitoloStudioForm.anaSpecialita.newSpecialita}"></html:text>
						</td>
    				</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.spectitolostudio.header.desSpecTitoloStudio" bundle="serviziLabels" />
				</td>
				<td align="left">
					<html:text styleId="testoNoBold" property="anaSpecialita.desSpecialita" size="50"
								maxlength="250"
								disabled="${DettaglioSpecTitoloStudioForm.conferma}"></html:text>
				</td>
			</tr>
		</table>
		</div>
		<br/>

		<div id="divFooter">
		<c:choose>
			<c:when test="${DettaglioSpecTitoloStudioForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/spectitolostudio/footerDettTitoloStudio.jsp" />
    		</c:otherwise>
		</c:choose>
		</div>
	</sbn:navform>
</layout:page>
