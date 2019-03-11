<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<br/>
<table width="100%" border="0">
	<tr>
		<td class="etichetta" valign="top"><bean:message
				key="ordine.label.fornitore" bundle="acquisizioniLabels" /></td>
		<td valign="top" colspan="4" scope="col" align="left"><html:text
				styleId="testoNormale" property="codFornitore" size="5"
				maxlength="10"></html:text> <html:text styleId="testoNormale"
				property="fornitore" size="40" maxlength="40"></html:text> <html:submit
				styleClass="buttonImage" property="methodStampaFascicoli">
				<bean:message key="ricerca.btn.fornitore.sif"
					bundle="acquisizioniLabels" />
			</html:submit></td>

	</tr>
	<tr>
		<!--
		<td valign="top" scope="col" align="left">
			<div class="etichetta">
				<bean:message key="periodici.label.periodicita"
					bundle="gestioneStampeLabels" />
			</div>
		</td>
		<td valign="top" scope="col" align="left"><html:select
				styleClass="testoNormale" property="periodicita">
				<html:optionsCollection property="listaPeriodicita" value="codice"
					label="descrizione" />
			</html:select></td>
		 -->
		<td valign="top" scope="col" align="left">
			<div class="etichetta">
				<bean:message key="periodici.label.statoOrdine"
					bundle="gestioneStampeLabels" />
			</div>
		</td>
		<td valign="top" scope="col" align="left"><html:select
				styleClass="testoNormale" property="statoOrdine">
				<html:optionsCollection property="listaStatoOrdine" value="codice"
					label="descrizione" />
			</html:select></td>
		<td valign="top" scope="col" align="left">
			<div class="etichetta">
				<bean:message key="periodici.label.tipoOrdine"
					bundle="gestioneStampeLabels" />
			</div>
		</td>
		<td valign="top" scope="col" align="left"><html:select
				styleClass="testoNormale" property="tipoOrdine">
				<html:optionsCollection property="listaTipoOrdine" value="codice"
					label="descrizione" />
			</html:select></td>
	</tr>
</table>
<br/>