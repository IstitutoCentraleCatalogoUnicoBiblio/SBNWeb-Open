<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<table width="100%" border="0">
	<BR>
	<tr>
		<td valign="top" scope="col" align="left">
			<div class="etichetta">
				<bean:message key="ricerca.label.annoOrdine"
					bundle="acquisizioniLabels" />
			</div></td>
		<!--	onchange="this.form.submit();"-->

		<td valign="top" scope="col" align="left"><html:text
				styleId="testoNormale" property="anno" size="4" maxlength="4" />
		</td>
		<td scope="col">
			<div style="text-align: right;" class="etichetta">
				<bean:message key="ricerca.label.tipoOrdine"
					bundle="acquisizioniLabels" />
			</div>
		</td>
		<td scope="col">
			<div align="left" class="etichetta">
				<html:select styleClass="testoNormale" property="tipo">
					<html:optionsCollection property="listaTipo" value="codice"
						label="descrizione" />
				</html:select>
			</div>
		</td>
		<td valign="top" scope="col" colspan="2"><div align="left"
				class="etichetta">
				<bean:message key="ordine.label.numero" bundle="acquisizioniLabels" />
				<html:text styleId="testoNormale" property="codice" size="5" maxlength="9" />
				 <html:submit styleClass="buttonImage"
					property="methodStampaFascicoli" disabled="${noinput}">
					<bean:message key="ricerca.button.ordine"
						bundle="acquisizioniLabels" />
				</html:submit>
			</div></td>
	</tr>
	<BR>
</table>
