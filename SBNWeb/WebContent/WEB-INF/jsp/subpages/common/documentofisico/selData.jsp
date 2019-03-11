<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<table width="100%" border="0">
	<tr>
		<td width="10%"><bean:message key="bollettino.dataDa"
				bundle="gestioneStampeLabels" />
				</td><td>&nbsp;&nbsp; <html:text
				styleId="testoNormale" property="dataDa" size="10" maxlength="10">
			</html:text></td>
		<td><bean:message key="bollettino.dataA"
				bundle="gestioneStampeLabels" />&nbsp;&nbsp; <html:text
				styleId="testoNormale" property="dataA" size="10" maxlength="10">
			</html:text></td>
	</tr>
	<c:if test="${navForm.codAttivita eq 'CS000' }">
		<tr>
			<td><bean:message key="bollettino.dataPrimaCollDa"
					bundle="gestioneStampeLabels" />
					</td><td>&nbsp;&nbsp; <html:text
					styleId="testoNormale" property="dataPrimaCollDa" size="10"
					maxlength="10">
				</html:text></td>
			<td><bean:message key="bollettino.dataA"
					bundle="gestioneStampeLabels" />&nbsp;&nbsp; <html:text
					styleId="testoNormale" property="dataPrimaCollA" size="10"
					maxlength="10">
				</html:text></td>
		</tr>
	</c:if>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td><bean:message key="bollettino.nuoviTitoli"
				bundle="gestioneStampeLabels" /> <html:radio property="check"
				value="nuoviTitoli" /></td>
		<td><bean:message key="bollettino.nuoviEsemplari"
				bundle="gestioneStampeLabels" /> <html:radio property="check"
				value="nuoviEsemplari" /></td>
	</tr>
</table>
<%-- <table width="100%" border="0">
	<tr>
		<td class="etichetta"></td>
		<td class="testoNormale"></td>
		<td class="etichetta"><bean:message
				key="documentofisico.ordinamento" bundle="documentoFisicoLabels" />
		</td>
		<td class="testoNormale"><html:select property="tipoOrdinamento">
				<html:optionsCollection property="listaTipiOrdinamento"
					value="codice" label="descrizione" />
			</html:select>
		</td>
	</tr>
</table>--%>
