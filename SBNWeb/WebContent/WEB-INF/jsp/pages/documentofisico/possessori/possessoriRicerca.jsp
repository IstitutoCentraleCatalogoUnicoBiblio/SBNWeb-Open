<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Pssessori
		Alessandro Segnalini - Inizio Codifica Marzo 2008
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>


<html:xhtml/>
<layout:page>
	<sbn:navform action="/documentofisico/possessori/possessoriRicerca.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="documentofisico.nome"
					bundle="documentoFisicoLabels" /> :</td>
				<td class="testoNormale"><html:text property="ricercaPoss.nome" size="70"
					maxlength="80"></html:text> <sbn:tastiera limit="80" property="ricercaPoss.nome"
					name="possessoriRicercaForm"></sbn:tastiera></td>
				<td class="etichetta"><bean:message key="documentofisico.inizio"
					bundle="documentoFisicoLabels" /> <html:radio property="ricercaPoss.tipoRicerca"
					value="inizio" /> <bean:message key="documentofisico.intero"
					bundle="documentoFisicoLabels" /> <html:radio property="ricercaPoss.tipoRicerca"
					value="intero" /> <bean:message key="documentofisico.parole"
					bundle="documentoFisicoLabels" /> <html:radio property="ricercaPoss.tipoRicerca"
					value="parole" /></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="documentofisico.pid"
					bundle="documentoFisicoLabels" />:</td>
				<td width="60" class="testoNormale"><html:text property="ricercaPoss.pid" size="10"
					maxlength="10"></html:text></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="documentofisico.tiponome"
					bundle="documentoFisicoLabels" />:</td>
				<td width="100"><bean:message key="documentofisico.tuttiNomi"
					bundle="documentoFisicoLabels" /> <html:radio property="tipoAutore"
					value="tuttiNomi" onchange="this.form.submit()" /></td>
				<td width="180"><bean:message key="documentofisico.nomePersonale"
					bundle="documentoFisicoLabels" /> <html:radio property="tipoAutore"
					value="autorePersonale" onchange="this.form.submit()" /></td>
				<td><bean:message key="documentofisico.nomeCollettivo"
					bundle="documentoFisicoLabels" /> <html:radio property="tipoAutore"
					value="autoreCollettivo" onchange="this.form.submit()" /></td>
			</tr>
			<tr>
				<td width="90"></td>
				<td width="100" class="etichetta">
				<td width="180" class="etichetta"><label for="tiponomeA"> <bean:message
					key="documentofisico.tiponomeA" bundle="documentoFisicoLabels" /> </label> <html:checkbox
					styleId="chkTipoNomeA" property="ricercaPoss.chkTipoNomeA"> </html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeA" value="false" /> <label for="tiponomeB">
				<bean:message key="documentofisico.tiponomeB" bundle="documentoFisicoLabels" /> </label> <html:checkbox
					styleId="chkTipoNomeB" property="ricercaPoss.chkTipoNomeB"></html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeB" value="false" /> <label for="tiponomeC">
				<bean:message key="documentofisico.tiponomeC" bundle="documentoFisicoLabels" /> </label> <html:checkbox
					styleId="chkTipoNomeC" property="ricercaPoss.chkTipoNomeC"></html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeC" value="false" /> <label for="tiponomeD">
				<bean:message key="documentofisico.tiponomeD" bundle="documentoFisicoLabels" /> </label> <html:checkbox
					styleId="chkTipoNomeD" property="ricercaPoss.chkTipoNomeD"></html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeD" value="false" /></td>
				<td><label for="tiponomeE"><bean:message key="documentofisico.tiponomeE"
					bundle="documentoFisicoLabels" /> </label> <html:checkbox styleId="chkTipoNomeE"
					property="ricercaPoss.chkTipoNomeE"></html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeE" value="false" /> <label for="tiponomeR"><bean:message
					key="documentofisico.tiponomeR" bundle="documentoFisicoLabels" /> </label> <html:checkbox
					styleId="chkTipoNomeR" property="ricercaPoss.chkTipoNomeR"></html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeR" value="false" /> <label for="tiponomeG"><bean:message
					key="documentofisico.tiponomeG" bundle="documentoFisicoLabels" /> </label> <html:checkbox
					styleId="chkTipoNomeG" property="ricercaPoss.chkTipoNomeG"></html:checkbox> <html:hidden
					property="ricercaPoss.chkTipoNomeG" value="false" /></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90"><bean:message key="documentofisico.forma" bundle="documentoFisicoLabels" />:</td>
				<td><bean:message key="documentofisico.tuttiNomi" bundle="documentoFisicoLabels" /><html:radio
					property="ricercaPoss.formaAutore" value="tutti" /> <bean:message
					key="documentofisico.autore" bundle="documentoFisicoLabels" /><html:radio
					property="ricercaPoss.formaAutore" value="autore" /> <bean:message
					key="documentofisico.rinvio" bundle="documentoFisicoLabels" /><html:radio
					property="ricercaPoss.formaAutore" value="rinvio" /></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90"  title="Ricerca soltanto possessori legati ad inventari della biblioteca operante">
				<bean:message key="documentofisico.soloBib"
					bundle="documentoFisicoLabels" />:</td>
				<td><html:checkbox property="ricercaPoss.soloBib"></html:checkbox>
				<html:hidden property="ricercaPoss.soloBib" value="false" /></td>
			</tr>
		</table>
		</div>
		<div id="divFooterCommon">
		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message key="documentofisico.elementiBlocco"
					bundle="documentoFisicoLabels" /></td>
				<td width="150" class="testoNormale"><html:text property="ricercaPoss.NRec" size="5"></html:text></td>
				<td width="75" class="etichetta"><bean:message key="documentofisico.ordinamento"
					bundle="documentoFisicoLabels" /></td>
				<td width="150" class="testoNormale"><html:select
					property="ricercaPoss.tipoOrdinamSelez" style="width:150px">
					<html:optionsCollection property="ricercaPoss.listaTipiOrdinam" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodPossRicerca">
					<bean:message key="ricerca.button.cerca" bundle="documentoFisicoLabels" />
				</html:submit></td>
				<c:choose>
					<c:when test="${possessoriRicercaForm.creaPoss eq 'SI'}">
						<td align="center"><sbn:checkAttivita idControllo="possessori">
							<html:submit property="methodPossRicerca">
								<bean:message key="button.creaPoss" bundle="documentoFisicoLabels" />
							</html:submit>
						</sbn:checkAttivita></td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
