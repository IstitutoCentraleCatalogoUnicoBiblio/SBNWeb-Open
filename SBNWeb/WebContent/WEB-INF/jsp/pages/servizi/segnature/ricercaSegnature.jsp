<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"           prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/segnature/RicercaSegnature">
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
						<html:text styleId="testoNoBold" property="dettRicercaSegn.codBiblioteca" size="5" readonly="true"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.segn" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dettRicercaSegn.segnInizio" readonly="false"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.fruiz" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:select property="dettRicercaSegn.codFruizione">
							<html:optionsCollection property="lstFruizioni" value="codice" label="descrizioneCodice" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.indisp" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:select property="dettRicercaSegn.codIndisp">
							<html:optionsCollection property="lstIndisp" value="codice" label="descrizioneCodice" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>

		<div id="divFooterCommon">
			<table width="100%" border="0" style="height:40px">
				<tr>
					<td width="80" class="etichetta">
						<bean:message key="documentofisico.elementiBlocco" bundle="documentoFisicoLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:text property="dettRicercaSegn.elemPerBlocchi" size="5" maxlength="4"></html:text>
					</td>
					<td width="75" class="etichetta">
						<bean:message key="documentofisico.ordinamento" bundle="documentoFisicoLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:select property="dettRicercaSegn.tipoOrdinamento" style="width:70px">
							<html:optionsCollection property="dettRicercaSegn.lstTipiOrdinamento" value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">

						<html:submit property="methodRicSegna">
							<bean:message key="servizi.bottone.cerca" bundle="serviziLabels" />
						</html:submit>

						<c:if test="${RicercaSegnatureForm.nonTrovato}">
							<html:submit property="methodRicSegna">
								<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
							</html:submit>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
