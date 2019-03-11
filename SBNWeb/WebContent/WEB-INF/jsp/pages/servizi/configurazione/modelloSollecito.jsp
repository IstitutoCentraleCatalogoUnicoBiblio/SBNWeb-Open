<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/configurazione/modelloSollecito.do">

		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>

			<br />
			<sbn:disableAll disabled="${navForm.conferma}">
			<table>
				<tr>
					<td>
						<jsp:include page="/WEB-INF/jsp/subpages/servizi/configurazione/folderModelloSollecito.jsp" />
						<html:textarea styleId="inputField" property="inputField" rows="20"	cols="80" />
					<td>
					<td class="testoTop">
						<p>Campi disponibili:</p>
						<noscript>
							<table class="sintetica">
								<l:iterate id="item" name="navForm" property="listaCampiModello" indexId="idx">
								<sbn:rowcolor var="color" index="idx" />
								<tr bgcolor="${color}">
									<td><bs:write name="item" property="codice"/></td>
									<td><bs:write name="item" property="descrizione"/></td>
								</tr>
								</l:iterate>
							</table>
						</noscript>
						<div class="js-only">
							<html:select property="campo" styleClass="w10em">
								<html:optionsCollection property="listaCampiModello" value="codice" label="descrizione" />
							</html:select>
							<button type="button" title="Aggiungi" onclick="var text = campo.options[campo.selectedIndex].value;insertAtCursor(inputField, text);">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels"/>
							</button>
						</div>
						<c:if test="${navForm.folder ne 'SMS'}">
							<p>Formattazione:</p>
							<noscript>
								<table class="sintetica">
									<l:iterate id="item" name="navForm" property="listaFormattazione" indexId="idx">
									<sbn:rowcolor var="color" index="idx" />
									<tr bgcolor="${color}">
										<td>
											&lsqb;<bs:write name="item" property="codice"/>&rsqb;...&lsqb;&sol;<bs:write name="item" property="codice"/>&rsqb;</td>
										<td><bs:write name="item" property="descrizione"/></td>
									</tr>
									</l:iterate>
								</table>
							</noscript>
							<div class="js-only">
								<html:select property="format" styleClass="w10em">
									<html:optionsCollection property="listaFormattazione" value="codice" label="descrizione" />
								</html:select>
								<button type="button" title="Aggiungi"
									onclick="var text = format.options[format.selectedIndex].value;wrapText('inputField', text);">
									<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels"/>
								</button>
							</div>
						</c:if>
					</td>
				</tr>

			</table>
			</sbn:disableAll>
		</div>
		<br />
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>

	</sbn:navform>
</layout:page>
