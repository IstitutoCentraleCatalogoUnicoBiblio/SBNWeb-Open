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
		<td td width="20%"><bean:message key="documentofisico.annoFatturaT"
				bundle="documentoFisicoLabels" />
		<html:text styleId="testoNormale" property="annoFattura"
				size="10" maxlength="4"></html:text>
		</td>
		<td><bean:message key="documentofisico.fatturaT"
				bundle="documentoFisicoLabels" /> <html:text styleId="testoNormale"
				property="progrFattura" size="15" maxlength="9"></html:text>
		</td>
	</tr>
	<BR>
	<tr>
		<td colspan="2"><bean:message key="documentofisico.ristampaBuonoCarico"
				bundle="documentoFisicoLabels" /> <html:checkbox
				property="ristampaDaFattura"></html:checkbox> <html:hidden
				property="ristampaDaFattura" value="false" /></td>
	</tr>
</table>
