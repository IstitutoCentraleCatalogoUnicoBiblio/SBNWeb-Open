<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="100%" border="0">
	<tr>
		<td>
			<html:textarea styleClass="expandedLabel"
				property="catalogazioneSemanticaComune.catalogazioneAbstract.descrizione"
				cols="90" rows="15" disabled="${CatalogazioneSemanticaForm.enableSoloEsamina}" />
			<sbn:tastiera limit="2160" name="CatalogazioneSemanticaForm"
				property="catalogazioneSemanticaComune.catalogazioneAbstract.descrizione"
				visible="${!CatalogazioneSemanticaForm.enableSoloEsamina} "/>
		</td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<td align="left" class="etichetta" scope="col">
			<bean:message key="esamina.inserito" bundle="gestioneSemanticaLabels" />
		</td>
		<td class="etichetta">
			<bean:message key="esamina.il" bundle="gestioneSemanticaLabels" />
		</td>
		<td>
			<html:text styleId="testoNormale"
				property="catalogazioneSemanticaComune.catalogazioneAbstract.dataInserimento"
				size="14" maxlength="20" readonly="true"></html:text>
		</td>
		<td class="etichetta">
			<bean:message key="esamina.modificato"
				bundle="gestioneSemanticaLabels" />
		</td>
		<td class="etichetta">
			<bean:message key="esamina.il" bundle="gestioneSemanticaLabels" />
		</td>
		<td>
			<html:text styleId="testoNormale"
				property="catalogazioneSemanticaComune.catalogazioneAbstract.dataVariazione"
				size="14" maxlength="20" readonly="true"></html:text>
		</td>
		<td class="etichetta">
			<bean:message key="crea.statoDiControllo"
				bundle="gestioneSemanticaLabels" />
		</td>
		<td>
			<html:select styleClass="testoNormale"
				property="catalogazioneSemanticaComune.catalogazioneAbstract.livelloAutorita"
				disabled="${CatalogazioneSemanticaForm.enableSoloEsamina}" >
				<html:optionsCollection property="listaStatoControllo"
					value="codice" label="descrizione" />
			</html:select>
		</td>
	</tr>
</table>


