<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<table width="100%" border="0">
	<tr>
		<td valign="top" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="soggettario.label.opzMan" bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col">
		<div align="left"><html:select styleClass="testoNormale"
			property="parametri.stampaDescrittoriManuali" style="width:40px">
			<html:optionsCollection property="listaSiNo" value="codice"
				label="descrizione" />
		</html:select></div>
		</td>
	</tr>

	<tr>
		<td valign="top" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="soggettario.label.opzColl" bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col">
		<div align="left"><html:select styleClass="testoNormale"
			property="parametri.stampaTitoli" style="width:40px">
			<html:optionsCollection property="listaSiNo" value="codice"
				label="descrizione" />
		</html:select></div>
		</td>
	</tr>

	<tr>
		<td valign="top" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="soggettario.label.opzLegame" bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col">
		<div align="left"><html:select styleClass="testoNormale"
			property="parametri.stampaNoteLegameTitoli" style="width:40px">
			<html:optionsCollection property="listaSiNo" value="codice"
				label="descrizione" />
		</html:select></div>
		</td>
	</tr>

	<tr>
		<td valign="top" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="soggettario.label.opzBibl"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col">
		<div align="left"><html:select styleClass="testoNormale"
			property="parametri.stampaSoloUtilizzati" style="width:40px">
			<html:optionsCollection property="listaSiNo"
				value="codice" label="descrizione" />
		</html:select></div>
		</td>
	</tr>

	<%--<tr>
		<td valign="top" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="soggettario.label.opzNote" bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col">
		<div align="left"><html:select styleClass="testoNormale"
			property="parametri.stampaNote" style="width:40px">
			<html:optionsCollection property="listaSiNo" value="codice"
				label="descrizione" />
		</html:select></div>
		</td>
	</tr> --%>

	<tr>
		<td valign="top" scope="col">
		<div align="left" class="etichetta"><bean:message
			key="soggettario.label.opzStringa"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col">
		<div align="left"><html:select styleClass="testoNormale"
			property="parametri.stampaSoloStringaSoggetto" style="width:40px">
			<html:optionsCollection property="listaSiNo"
				value="codice" label="descrizione" />
		</html:select></div>
		</td>
	</tr>

</table>
