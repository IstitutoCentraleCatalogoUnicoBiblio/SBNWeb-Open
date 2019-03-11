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
	<tr>
		<td width="37%" class="testo">
		<div align="center"><bean:message key="documentofisico.stampaTitT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td width="33%"><html:checkbox property="stampaTit"></html:checkbox><html:hidden
			property="stampaTit" value="false" /></td>
		<td width="4%"></td>
		<td width="26%">&nbsp;</td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<td width="22%" class="testo">Cataloghi attivi</td>
		<td width="13%" class="testo">
		<div align="right"><bean:message key="documentofisico.autoriT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td width="4%" class="testo"><html:checkbox property="autori"></html:checkbox><html:hidden
			property="autori" value="false" /></td>
		<td width="20%" class="testo"><html:select property="sceltaAutori">
			<html:optionsCollection property="listaSceltaAutori" value="codice"
				label="descrizione" />
		</html:select></td>
		<td width="17%" class="testo">
		<div align="right"><bean:message key="documentofisico.editoriT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td width="4%" class="testo"><html:checkbox property="editori"></html:checkbox><html:hidden
			property="editori" value="false" /></td>
		<td width="20%" class="testo">&nbsp;</td>
	</tr>
	<tr>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.topograficoT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="topografico"></html:checkbox><html:hidden
			property="topografico" value="false" /></td>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.possessoriT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="possessori"></html:checkbox><html:hidden
			property="possessori" value="false" /></td>
		<td class="testo">&nbsp;</td>
	</tr>
	<tr>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.soggettiT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="soggetti"></html:checkbox><html:hidden
			property="soggetti" value="false" /></td>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.classificazioniT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="classificazioni"></html:checkbox><html:hidden
			property="classificazioni" value="false" /></td>
		<td class="testo">&nbsp;</td>
	</tr>
	<tr>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.titoliT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="titoli"></html:checkbox><html:hidden
			property="titoli" value="false" /></td>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"></div>
		</td>
		<td class="testo"></td>
		<td class="testo">&nbsp;</td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<td colspan="2" class="testo">Collocazione sulla scheda</td>
		<td width="4%" class="testo">&nbsp;</td>
		<td width="20%" class="testo">N. Copie</td>
		<td width="17%" class="testo">
		<div align="right"></div>
		</td>
		<td width="4%" class="testo">&nbsp;</td>
		<td width="20%" class="testo">N. Copie</td>
	</tr>
	<tr>
		<td width="22%" class="testo">&nbsp;</td>
		<td width="13%" class="testo">
		<div align="right"><bean:message key="documentofisico.principaleT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="principale"></html:checkbox><html:hidden
			property="principale" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copiePrincipale" size="10"></html:text></td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.editoriT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="editori2"></html:checkbox><html:hidden
			property="editori2" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copieEditori2" size="10"></html:text></td>
	</tr>
	<tr>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.topograficoT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="topografico2"></html:checkbox><html:hidden
			property="topografico2" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copieTopografico2" size="10"></html:text></td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.possessoriT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="possessori2"></html:checkbox><html:hidden
			property="possessori2" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copiePossessori2" size="10"></html:text></td>
	</tr>
	<tr>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.soggettiT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="soggetti2"></html:checkbox><html:hidden
			property="soggetti2" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copieSoggetti2" size="10"></html:text></td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.classificazioniT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="classificazioni2"></html:checkbox><html:hidden
			property="classificazioni2" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copieClassificazioni2" size="10"></html:text></td>
	</tr>
	<tr>
		<td class="testo">&nbsp;</td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.titoliT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="titoli2"></html:checkbox><html:hidden
			property="titoli2" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copieTitoli2" size="10"></html:text></td>
		<td class="testo">
		<div align="right"><bean:message key="documentofisico.richiamiT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td class="testo"><html:checkbox property="richiami"></html:checkbox><html:hidden
			property="richiami" value="false" /></td>
		<td width="20%" class="testo"><html:text styleId="testo"
			property="copieRichiami" size="10"></html:text></td>
	</tr>
</table>
