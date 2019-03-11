<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/spectitolostudio/RicercaSpecTitoloStudio">
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
						<html:text styleId="testoNoBold" property="datiRicerca.codBib" size="5" readonly="true"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.spectitolostudio.header.titoloStudio" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:select property="datiRicerca.titoloStudio" disabled="${datiRicerca.newSpecialita}">
							<html:optionsCollection property="listaTdS" value="descrizioneCodice" label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message
							key="servizi.spectitolostudio.header.codSpecTitoloStudio" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="datiRicerca.codSpecialita" size="2" maxlength="2"
									readonly="${datiRicerca.newSpecialita}"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.spectitolostudio.header.desSpecTitoloStudio" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="datiRicerca.desSpecialita" size="50" maxlength="250" readonly="false"></html:text>
					</td>
				</tr>
			</table>
		</div>
		<br/>
		<br/>

		<div id="divFooterCommon">
			<table width="100%" border="0" style="height:40px">
				<tr>
					<td width="80" class="etichetta">
						<bean:message key="documentofisico.elementiBlocco" bundle="documentoFisicoLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:text property="datiRicerca.numeroElementiBlocco" size="5"></html:text>
					</td>
					<td width="75" class="etichetta">
						<bean:message key="documentofisico.ordinamento" bundle="documentoFisicoLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:select property="datiRicerca.ordinamento" >
							<html:optionsCollection property="datiRicerca.lstTipiOrdinamento"
													value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>
		<br/>

		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodRicSpecialita">
							<bean:message key="servizi.bottone.cerca" bundle="serviziLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
